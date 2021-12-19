# spring-boot-api-date
A Simple Serverless Java Spring Boot REST API on AWS Lambda and API Gateway (GET method) which returns the server date, time, timezone, and epoch time in json format.

## To Deploy
File Jenkinsfile-aws is a jenkinsfile configuration used by Jenkins pipeline to automatically perform the following stages:

- Using Gradle Wrapper, build and test the Java Spring Boot serverless REST API and outputs a zip file at build/distributions folder
```bash
./gradlew clean build
```  
- Using Cloudformation template sam-template.yaml, upload the Spring Boot artifact to S3 bucket  
```bash
aws cloudformation package --template-file sam-template.yaml --s3-bucket {{bucket-name}} --output-template-file springboot-serverdate.yaml --region {{region}}
```
- Deploy the Cloudformation stack to create a Lambda function and API Gateway
```bash
aws cloudformation deploy --template-file springboot-serverdate.yaml --stack-name ServerDate-Api --region {{region}} --capabilities CAPABILITY_IAM
```     
- Get the API Gateway Endpoint to test the actual API
```bash
aws cloudformation describe-stacks --stack-name ServerDateApi --region {{region}} --query \'Stacks[0].Outputs[*].{Service:OutputKey,Endpoint:OutputValue}\'
```

## To Test
To test if the API is working, visit the AWS API Gateway Endpoint in your chosen browser or perform the CURL command. The expected result is the server date, time, and timezone in json format. The server mentioned here would be the EC2 instance (managed by AWS, not the user) where the Lambda function is running.

Example Test Command:
    ```bash
    curl -X GET https://xxxxxxxxx.execute-api.us-east-2.amazonaws.com/Prod/serverdate
    ```

Example Test Result:
    ```json
    {"date":"03-18-2021","time":"08:43:48","timezone":"UTC+0000","epoch":"1616057028"}
    ```

NOTE: There will be a few seconds delay in showing the actual data the first time due to cold start of the Lambda function. Additional code improvements can be included to keep the Lambda function warm. 

# The Pre-requisites
- Java 8 or above
- Spring Framework 5.3.4 or above
- Gradle 6 or above
- AWS Cli
- AWS SAM Cli


# The Solution
## Dependencies
1. In build.gradle, Add the aws-serverless-java-container-spring framework as a dependency to deploy the Spring Boot application to Lambda
  - 'com.amazonaws.serverless:aws-serverless-java-container-spring:1.1'
  - Serverless Java Container Library acts as a proxy between Lambda runtime that translates incoming requests to request objects that framework can understand, and transforms responses from the application into a format that API Gateway understands.

## Controller
2. In src/main/java/com/example/controller/ServerDateController.java, the RestController annotation is used.
  - First, java.time package is imported to use for getting the required date, time, timezone, and epoch.
  - DateTimeFormatter is used to format the output to more human readable results
  - Epoch is in long data type. the return output expects a string data type. Convert long to string.
  - GET /serverdate produces JSON

## Lambda Handler
3. StreamLambdaHandler.java is the Lambda handler method that runs when the function is invoked.
  - at Lambda runtime, object is being converted from a received JSON-formatted string event and passes it to the Lambda handler. You set which method to invoke by setting the Handler parameter in Lambda function's configuration:
    - Properties:
      Handler: com.example.StreamLambdaHandler
  - The interface for the handler method used is the RequestStreamHandler which accepts input and output streams, which uses Gson library for serialization and deserialization.  

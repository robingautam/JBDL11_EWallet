# JBDL11_EWallet

# Create User API
postman request POST 'http://localhost:8080/user-service/create/user' \
  --header 'Content-Type: application/json' \
  --body '{
    "name": "Satyam",
    "email": "satyampandey7248@gmail.com",
    "phoneNo": "8790345680",
    "password": "123456",
    "userIdentifier": "AADHAR_CARD",
    "userIdentifierValue": "778956146525",
    "dob": "14/08/1998"
}'

# OTP Verification API
postman request POST 'http://localhost:8080/user-service/validate/otp' \
  --header 'Content-Type: application/json' \
  --body '{
    "email": "satyampandey7248@gmail.com",
    "otp": "394790"
}'

# Transaction History API
postman request 'http://localhost:8083/txn-service/get/txn/history' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic ODc5MDM0NTY4MDoxMjM0NTY=' \
  --body '{
    "receiverId": "8790345679",
    "amount": 102,
    "purpose": "Test Transfer"
}' \
  --auth-basic-username '8790345680' \
  --auth-basic-password '123456'

# Transaction Initiate
postman request POST 'http://localhost:8083/txn-service/initiate/transaction' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic ODc5MDM0NTY4MDoxMjM0NTY=' \
  --body '{
    "receiverId": "8790345679",
    "amount": 102,
    "purpose": "Test Transfer"
}' \
  --auth-basic-username '8790345680' \
  --auth-basic-password '123456'

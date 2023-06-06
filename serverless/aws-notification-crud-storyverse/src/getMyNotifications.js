const AWS = require("aws-sdk");
const getMyNotifications = async (event) => {
    try {
        const dynamoDB = new AWS.DynamoDB.DocumentClient();
        const { owner } = event.pathParameters;
        const result = await dynamoDB.scan({
            TableName: "NotificationTable",
            FilterExpression: "#owner = :owner",
            ExpressionAttributeNames: {
                "#owner": "owner"
            },
            ExpressionAttributeValues: {
                ":owner": owner
            }
        }).promise(); 
        const notifications = result.Items;
        return {
            status: 200,
            body: {
                notifications
            }
        }
    } catch (error) {
        console.log(error);
    }
}
module.exports = {
    getMyNotifications
}
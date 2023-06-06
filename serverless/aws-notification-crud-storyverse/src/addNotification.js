const { v4 } = require('uuid');
const AWS = require('aws-sdk');
const addNotification = async (event) => {
    const dynamodb = new AWS.DynamoDB.DocumentClient();
    const { owner, title, description } = JSON.parse(event.body);
    const createdAt = new Date().toISOString();
    const id = v4();
    const newNotification = {
        id,
        owner,
        title,
        description,
        createdAt
    }
    await dynamodb.put({
        TableName: 'NotificationTable',
        Item: newNotification
    }).promise();
    return {
        status: 200,
        body: newNotification
    }
}
module.exports = {
    addNotification
}
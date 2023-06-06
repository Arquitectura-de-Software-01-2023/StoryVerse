const AWS = require('aws-sdk');
const s3 = new AWS.S3();

const { v4 } = require('uuid');

const BUCKET_NAME = 'aws-storyverse-bucket-dev';

module.exports.handler = async (event) => {
    console.log(event);

    const response = {
        isBase64Encoded: false,
        statusCode: 200,
        body: JSON.stringify({
            message: 'La imagen se subio correctamente',
        }),
    };

    try {
        const parsedBody = JSON.parse(event.body);
        const base64File = parsedBody.file;
        const decodedFile = Buffer.from(base64File.replace(/^data:image\/\w+;base64,/, ""), 'base64');
        const id = v4();
        const params = {
            Bucket: BUCKET_NAME,
            Key: `images/${id}.jpeg`,
            Body: decodedFile,
            ContentType: 'image/*',
        };

        const uploadResult = await s3.upload(params).promise();
        response.body = JSON.stringify({
            message: 'La imagen se subio correctamente',
            uploadResult,
        });

    } catch (error) {
        console.log(error);
        response.statusCode = 500;
        response.body = JSON.stringify({
            message: 'Hubo un error al subir la imagen',
        });
    }
    return response;
};
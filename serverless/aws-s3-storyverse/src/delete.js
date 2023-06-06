const AWS = require('aws-sdk'); // Importamos el SDK de AWS
const s3 = new AWS.S3();

const BUCKET_NAME = 'aws-storyverse-bucket-dev';

module.exports.handler = async (event) => {
    console.log(event);
    
    const response = {
        isBase64Encoded: false,
        statusCode: 200,
    };

    try {
        const key = decodeURIComponent(event.pathParameters.fileKey);
        const params = {
            Bucket: BUCKET_NAME,
            Key: `images/${key}`,
        };
        const deleteObject = await s3.deleteObject(params).promise();

        response.body = JSON.stringify({
            message: 'La imagen se elimino correctamente',
            deleteObject,
        });
    } catch (error) {
        console.log(error);
        response.statusCode = 500;
        response.body = JSON.stringify({
            message: 'Hubo un error al subir la imagen',
            error,
        });
    }
    return response;
};
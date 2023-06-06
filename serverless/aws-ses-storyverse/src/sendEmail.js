const AWS = require('aws-sdk'); // Importar el SDK de AWS
const SES = new AWS.SES(); // Crear una instancia de SES

exports.handler = async (event) => {
    // Imprimir el evento en el registro
    console.log('event', event);

    // Campos del cuerpo de la solicitud
    const { to, from, subject, text } = JSON.parse(event.body);

    // Comprobar si los campos están vacíos
    if (!to || !from || !subject || !text) {
        return {
            statusCode: 400,
            body: JSON.stringify({ message: 'Faltan campos' }),
        };
    }

    // Crear los parámetros para el mensaje
    const params = {
        Destination: {
            ToAddresses: [to],
        },
        Message: {
            Body: {
                Text: { Data: text },
            },
            Subject: { Data: subject },
        },
        Source: from,
    };

    // Funcionalidad de envío de correo electrónico
    try {
        await SES.sendEmail(params).promise();
        return {
            statusCode: 200,
            body: JSON.stringify({ message: 'Correo electrónico enviado' }),
        };
    } catch (error) {
        return {
            statusCode: 500,
            body: JSON.stringify({ message: error.message }),
        };
    }
}
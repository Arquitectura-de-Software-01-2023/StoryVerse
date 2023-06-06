const AWS = require('aws-sdk');
const ses = new AWS.SES({ region: 'us-east-1' });

exports.handler = async (event, context) => {
  console.log('event', event);
  
  const { email } = JSON.parse(event.body); // Obtén la dirección de correo electrónico desde el evento o los datos de entrada

  try {
    const params = {
      EmailAddress: email
    };

    const result = await ses.verifyEmailAddress(params).promise();
    console.log('Solicitud de verificación enviada:', result);

    return {
      statusCode: 200,
      body: 'Solicitud de verificación enviada'
    };
  } catch (error) {
    console.error('Error al enviar la solicitud de verificación:', error);
    return {
      statusCode: 500,
      body: 'Error al enviar la solicitud de verificación'
    };
  }
};

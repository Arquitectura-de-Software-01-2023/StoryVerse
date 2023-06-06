package com.example.msuser.util


class AuthUtil {
    /**
     * Método para obtener el token de un usuario autenticado
     * que se encuentra en la cabecera de la petición
     * @param headers
     * @return token
     */
    fun getTokenFromHeader(headers: Map<String, String>): String {
        if (headers["Authorization"] == null && headers["authorization"] == null) {
            throw Exception("No se ha enviado el token de autorización")
        }
        // Se acostumbra que cuando se envia el token, se lo envia en el siguiente formato
        // Authorization: Bearer TOKEN
        val jwt: String = if (headers["Authorization"] != null) {
            headers["Authorization"]!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        } else {
            headers["authorization"]?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }!!.toTypedArray()[1]
        }
        return jwt
    }

}
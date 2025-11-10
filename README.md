# Trustgate

## Descripción

Aplicación Android desarrollada con Kotlin y Jetpack Compose, que simula el acceso y verificación de identidad en un condominio inteligente. 
Implementa arquitectura MVVM por feature, con capas separadas de presentación, dominio y datos.

## Diagrama de flujo
<img width="1092" height="855" alt="image" src="https://github.com/user-attachments/assets/eb19f408-0cf3-41b8-bf30-eba5ec8bf152" />

## Reflexión sobre robustez, decisiones y limitaciones

La aplicación presenta un flujo de datos robusto al mantener separadas las responsabilidades entre la capa de datos, la lógica de negocio y la interfaz. El uso de MVVM permite que las pantallas reaccionen a cambios en el estado sin tener que depender directamente de Firebase o de la capa de almacenamiento local. Esto facilita el mantenimiento y reduce el riesgo de inconsistencias cuando se actualiza la información del usuario o cuando la aplicación se reanuda después de haber sido cerrada.

Las decisiones arquitectónicas se tomaron para asegurar claridad y extensibilidad. Firebase Authentication se utiliza para el manejo de cuentas, lo que evita implementar un sistema de autenticación desde cero. Firebase Storage se eligió para guardar la imagen del documento por ser una solución integrada y segura. Firestore se usa para registrar el estado de verificación del usuario, lo cual permite que el flujo de verificación sea retomado en el punto exacto donde se quedó, incluso después de cerrar la sesión o reinstalar la aplicación. DataStore se eligió para manejar estados simples y persistentes, como el último correo utilizado y si la sesión debe mantenerse activa, ya que es más seguro y moderno que SharedPreferences.

Entre las limitaciones, actualmente la verificación del ID es simulada, ya que solo se guarda la imágen en Storage pero no se revisa para comprobar que es un documento de verificación válido. Además, al no estar dentro del plan de la clase programar microcontroladores, el escaneo de QR y apertura de talanquera consiguiente también son simulados. Como mejora futura, se podría agregar verificación automática con reconocimiento OCR, autenticación con Google u otra red social, notificaciones push para avisar al usuario cuando su verificación haya sido aprobada y la funcionalidad real de abrir talanqueras y compartir la información con el guardia de seguridad.

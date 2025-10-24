# Trustgate

## Descripción

Aplicación Android desarrollada con Kotlin y Jetpack Compose, que simula el acceso y verificación de identidad en un condominio inteligente. 
Implementa arquitectura MVVM por feature, con capas separadas de presentación, dominio y datos.

## Diagrama de flujo
<img width="1092" height="855" alt="image" src="https://github.com/user-attachments/assets/eb19f408-0cf3-41b8-bf30-eba5ec8bf152" />

## Diseño y decisiones

Se utiliza una instancia de ViewModel por feature (p. ej. uno compartido para VerificationConsent y VerificationPhoto, otro para Home y otro para Login).
Los repositorios están desacoplados mediante interfaces de dominio, lo que permite cambiar la fuente de datos sin alterar la lógica de presentación.
La simulación de latencia y errores en los DataSources sirve para probar el manejo de estados de carga y error sin backend real.
El estado de UI se maneja con StateFlow y collectAsStateWithLifecycle para manenejar estados de manenera correcta y eficiente.

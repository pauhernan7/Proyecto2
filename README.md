# Descripción general

## Archivos fuente relevantes

- `app/build.gradle.kts`  
- `app/src/main/AndroidManifest.xml`  
- `app/src/main/java/com/example/projecte2/DashboardActivity.java`  
- `app/src/main/java/com/example/projecte2/MainActivity.java`  
- `app/src/main/java/com/example/projecte2/ProductoVendido.java`  
- `gradle/libs.versions.toml`  

Este documento proporciona una visión general completa de la aplicación de comercio electrónico Android, explicando su propósito, arquitectura, componentes clave y flujos de trabajo. La aplicación ofrece dos interfaces de usuario distintas: un panel administrativo para la gestión de productos y pedidos, y una tienda orientada al cliente para explorar y comprar productos.

Para obtener información detallada sobre los componentes específicos del sistema, consulta la sección Arquitectura del Sistema y sus subsecciones.

## Propósito y funcionalidad

La aplicación está diseñada para facilitar operaciones de venta en línea con las siguientes funciones clave:

- Autenticación de usuarios con acceso basado en roles (administrador/cliente)  
- Panel de administración con análisis de ventas  
- Gestión de catálogo de productos para administradores  
- Exploración y compra de productos para clientes  
- Sistema de carrito de compras  
- Seguimiento y gestión de pedidos  
- Sistema de tickets de soporte  

**Fuentes:**  
- `app/src/main/AndroidManifest.xml`  
- `app/src/main/java/com/example/projecte2/MainActivity.java`

# Visión general de la arquitectura del sistema

La aplicación sigue una arquitectura cliente-servidor con una API RESTful para la comunicación con el backend.

## Arquitectura a alto nivel

![Arquitectura](https://github.com/user-attachments/assets/700523f1-8a6a-4ac3-881b-6d9a05ded352)

**Fuentes:**  
- `app/src/main/java/com/example/projecte2/MainActivity.java`  
- `app/src/main/java/com/example/projecte2/DashboardActivity.java`

## Tecnologías utilizadas

| Componente              | Tecnología             | Propósito                                      |
|-------------------------|------------------------|------------------------------------------------|
| Interfaz de usuario     | Android XML Layouts    | Definición de la interfaz                      |
| Comunicación backend    | Retrofit               | Cliente para API REST                          |
| Análisis de datos       | Gson                   | Serialización/deserialización JSON             |
| Carga de imágenes       | Glide                  | Carga y caché eficiente de imágenes            |
| Gráficos                | MPAndroidChart         | Visualización de datos en el panel             |
| Logging de red          | OkHttp Interceptor     | Depuración de llamadas a la API                |
| Componentes de UI       | Material Design        | Elementos de UI modernos                       |

**Fuentes:**  
- `gradle/libs.versions.toml`  
- `app/build.gradle.kts`

## Flujo de usuario y navegación

La aplicación admite dos flujos de usuario distintos según el rol autenticado:

![Flujo de usuarios](https://github.com/user-attachments/assets/2c650bcc-d620-40e9-8d00-40fdb0fa5893)

**Fuentes:**  
- `app/src/main/AndroidManifest.xml`  
- `app/src/main/java/com/example/projecte2/MainActivity.java`  
- `app/src/main/java/com/example/projecte2/DashboardActivity.java`

## Autenticación y tipos de usuario

La aplicación admite dos roles de usuario:

- **Administrador**: acceso al panel de control, gestión de productos y pedidos, y sistema de soporte.  
- **Cliente**: puede explorar productos, agregar al carrito, realizar compras y enviar tickets de soporte.  

La autenticación se maneja mediante un sistema de tokens JWT, y las credenciales junto con el token se almacenan en `SharedPreferences` para mantener la sesión entre usos.

**Fuentes:**  
- `app/src/main/java/com/example/projecte2/MainActivity.java`

## Modelos de datos y comunicación

### Modelos de datos clave

![Modelos de datos](https://github.com/user-attachments/assets/7188b06f-a53e-450a-9755-3c050171d22c)

**Fuentes:**  
- `app/src/main/java/com/example/projecte2/ProductoVendido.java`  
- `app/src/main/java/com/example/projecte2/MainActivity.java`

### Comunicación con la API

La aplicación utiliza Retrofit para comunicarse con el servidor backend. La interfaz `ApiService` define los métodos de los endpoints, mientras que la clase `RetrofitClient` configura el cliente HTTP.

![API](https://github.com/user-attachments/assets/e4d430e5-2b5a-4efc-8d62-bfcf8925d9ca)

**Fuentes:**  
- `app/src/main/java/com/example/projecte2/MainActivity.java`  
- `app/src/main/java/com/example/projecte2/DashboardActivity.java`

## Panel de administración

El panel de administración es el centro de operaciones para funciones administrativas, que proporciona:

- Estadísticas de ventas con gráficos de pastel  
- Información sobre el producto más vendido  
- Listado de pedidos recientes  
- Navegación a otras secciones de administración  

Este panel utiliza la librería MPAndroidChart para visualizar datos de ventas extraídos de la API.

**Fuentes:**  
- `app/src/main/java/com/example/projecte2/DashboardActivity.java`

## Interfaz del cliente

La interfaz del cliente está centrada en la experiencia de compra:

- Exploración del catálogo de productos  
- Gestión del carrito de compras  
- Proceso de pago  
- Seguimiento de pedidos  
- Creación de tickets de soporte  

**Fuentes:**  
- `app/src/main/AndroidManifest.xml`

## Navegación y componentes de UI

La aplicación mantiene una navegación coherente a través de las pantallas:

- `HeaderFragment`: proporciona barra superior de navegación y título  
- `DrawerLayout` con `NavigationView`: menú lateral  
- `RecyclerView` con adaptadores personalizados: para mostrar listas de productos, pedidos y tickets  

**Fuentes:**  
- `app/src/main/java/com/example/projecte2/DashboardActivity.java`

## Configuración del proyecto

La aplicación está dirigida a la API 35 de Android (Android 15), con un mínimo de API 25 (Android 7.1). Utiliza Kotlin DSL para la configuración de Gradle y un sistema de gestión de dependencias basado en archivos `.toml`.

**Fuentes:**  
- `app/build.gradle.kts`  
- `gradle/libs.versions.toml`

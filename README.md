
# 🌿 Plantae - Aplicación Android de Comercio Electrónico

## 📄 Archivos fuente relevantes

📁 app/build.gradle.kts
📁 app/src/main/AndroidManifest.xml
📁 app/src/main/java/com/example/projecte2/DashboardActivity.java
📁 app/src/main/java/com/example/projecte2/MainActivity.java
📁 app/src/main/java/com/example/projecte2/ProductoVendido.java
📁 gradle/libs.versions.toml

Este documento proporciona una visión general de la aplicación Android de comercio electrónico **Plantae**, explicando su propósito, arquitectura, componentes clave y flujos de trabajo. La app ofrece dos interfaces diferentes:  
👨‍💼 Panel administrativo para la gestión de productos y pedidos  
🛍️ Tienda para clientes para explorar y comprar productos

Para más detalles técnicos, consulta la sección de **Arquitectura del sistema** y sus subsecciones.

---

## 🎯 Propósito y funcionalidad

La aplicación está diseñada para facilitar operaciones de venta en línea, e incluye las siguientes funciones:

- 🔐 Autenticación con control de acceso por roles (admin/cliente)  
- 📊 Panel de administración con estadísticas de ventas  
- 📦 Gestión del catálogo de productos  
- 🛒 Navegación y compra de productos  
- 🛍️ Carrito de compras  
- 🚚 Seguimiento de pedidos  
- 🆘 Sistema de soporte mediante tickets

📌 Fuentes:  
`AndroidManifest.xml`, `MainActivity.java`

---

## 🏗️ Arquitectura del sistema

La app sigue una arquitectura cliente-servidor, comunicándose con el backend mediante una API REST.

### 🧱 Arquitectura a alto nivel

![Arquitectura](https://github.com/user-attachments/assets/700523f1-8a6a-4ac3-881b-6d9a05ded352)

📌 Fuentes:  
`MainActivity.java`, `DashboardActivity.java`

---

## 🧰 Tecnologías utilizadas

| Componente              | Tecnología         | Propósito                                      |
|-------------------------|--------------------|-----------------------------------------------|
| 🖼️ Interfaz UI         | Android XML        | Definición de interfaces                      |
| 🔌 Comunicación backend | Retrofit           | Cliente para API REST                         |
| 🔄 Parsing de datos     | Gson               | Serialización/Deserialización de JSON         |
| 🖼️ Carga de imágenes    | Glide              | Carga y caché eficiente de imágenes           |
| 📊 Gráficas             | MPAndroidChart     | Visualización de datos (ventas, productos)    |
| 🐛 Logs de red          | OkHttp Interceptor | Depuración de peticiones API                  |
| 🎨 Componentes UI       | Material Design    | Elementos modernos de interfaz                |

📌 Fuentes:  
`libs.versions.toml`, `build.gradle.kts`

---

## 🧭 Flujos de usuario y navegación

La app admite dos flujos de usuario distintos según su rol de autenticación:

![User Flow](https://github.com/user-attachments/assets/2c650bcc-d620-40e9-8d00-40fdb0fa5893)

📌 Fuentes:  
`AndroidManifest.xml`, `MainActivity.java`, `DashboardActivity.java`

---

## 👥 Tipos de usuario y autenticación

La aplicación distingue entre dos tipos de usuarios:

- 👨‍💼 **Administrador** – Accede al dashboard, gestiona productos, pedidos y soporte
- 🛒 **Cliente** – Explora productos, añade al carrito, realiza compras y abre tickets

La autenticación usa tokens JWT, y los datos de sesión se almacenan en `SharedPreferences`.

📌 Fuente:  
`MainActivity.java`

---

## 🗂️ Modelos de datos y comunicación

### 📦 Modelos de datos principales

![Data Models](https://github.com/user-attachments/assets/7188b06f-a53e-450a-9755-3c050171d22c)

📌 Fuente:  
`ProductoVendido.java`, `MainActivity.java`

### 🔁 Comunicación API

Retrofit se encarga de la comunicación con el servidor.  
- `ApiService` define los endpoints  
- `RetrofitClient` configura el cliente HTTP

![API Communication](https://github.com/user-attachments/assets/e4d430e5-2b5a-4efc-8d62-bfcf8925d9ca)

📌 Fuentes:  
`MainActivity.java`, `DashboardActivity.java`

---

## 📊 Panel de administración

El dashboard centraliza las funciones del administrador:

- Estadísticas de ventas con gráficos  
- Producto más vendido  
- Lista de pedidos recientes  
- Navegación a secciones administrativas

Utiliza **MPAndroidChart** para visualizar los datos desde la API.

📌 Fuente:  
`DashboardActivity.java`

---

## 🛍️ Interfaz del cliente

La interfaz del cliente permite:

- Navegar por el catálogo de productos  
- Gestionar el carrito  
- Realizar el proceso de compra  
- Ver pedidos y crear tickets de soporte

📌 Fuente:  
`AndroidManifest.xml`

---

## 🧩 Navegación y componentes UI

- `HeaderFragment`: navegación superior con título  
- `DrawerLayout` y `NavigationView`: menú lateral  
- `RecyclerView`: muestra listas (productos, pedidos, tickets)

📌 Fuente:  
`DashboardActivity.java`

---

## ⚙️ Configuración del proyecto

- 🎯 API Target: Android 15 (API 35)  
- 📉 Mínimo SDK: Android 7.1 (API 25)  
- 🛠️ Build system: Kotlin DSL y gestión de dependencias TOML

📌 Fuentes:  
`build.gradle.kts`, `libs.versions.toml`

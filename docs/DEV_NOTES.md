# Empaquetado de la aplicación

Este proyecto es una mezcla de Spring Boot (para la lógica y base de datos) y JavaFX (para la interfaz gráfica).
Lo que vas a encontrar aquí es cómo correr la app y cómo convertirla en un instalador


Cómo correr la app en el IDE
- Abre el proyecto en tu IDE favorito.
- Corre la clase `FinanciaFx`, esa es la que arranca todo: Spring Boot + JavaFX.
- Asegúrate de tener tus archivos `.fxml` en `src/main/resources/templates y las imágenes en src/main/resources/Imagenes`

Puedes cambiar las imagenes por las que tu creas convenientes segun tus necesidades

## Generar el runtime con `jlink`

Antes de crear el instalador, necesitas una versión reducida de Java que contenga solo los módulos que tu app necesita.
El comando típico para powershell es:

```
jlink --module-path "$env:JAVA_HOME\jmods;C:\JavaFX\javafx-jmods-23" `
>>       --add-modules java.base,java.desktop,java.sql,javafx.base,javafx.graphics,javafx.controls,javafx.fxml,java.logging,java.xml,java.naming,java.management,java.instrument,java.net.http,java.compiler `                                                                                                                                                          
>>       --output runtime                            
```

### Explicación
- `--module-path` -> Le dices dónde están los módulos de Java (`$env:JAVA_HOME\jmods`) y los de JavaFX (`C:\JavaFX\javafx-jmods-23`). Tienes que ajustar esto segun la 
ruta donde tengas estos modulos, para los jmods de jafafx puedes descargarlos desde su pagina oficial en https://gluonhq.com/products/javafx/, descarga la version de jmods
segun la version de jdk que estes usando para crear tu app.
- `--add-modules` -> Lista de módulos que tu app necesita (JavaFX, JDBC, logging, etc.).
- `--output runtime` -> Es la carpeta donde se guarda la runtime personalizada.

- Esa carpeta runtime es la que luego usará el plugin `jpackage` para crear el instalador.



## Compilar con Maven
Ahora que ya tenemos nuestro runtime personalizado a nivel de la raiz del proyecto, toca hacer el empaquetado de nuestra app para generar nuestro `.exe` del instalador.

### Empaquetado en instalador .exe
El truco está en usar el plugin `jpackage-maven-plugin`.
En tu `pom.xml` debe existir algo como esto:

```xml
<plugin>
    <groupId>org.panteleyev</groupId>
    <artifactId>jpackage-maven-plugin</artifactId>
    <version>1.6.0</version>
    <executions>
        <execution>
            <id>jpackage</id>
            <phase>package</phase>
            <goals>
                <goal>jpackage</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <name>Financia</name>
        <input>${project.build.directory}</input>
        <destination>${project.build.directory}</destination>
        <mainJar>Financia-1.0.0.jar</mainJar>
        <type>EXE</type>
        <appVersion>1.0.0</appVersion>
        <icon>${project.basedir}/src/main/resources/Imagenes/icon.ico</icon>
        <runtimeImage>${project.basedir}/runtime</runtimeImage>
        <winConsole>true</winConsole>
        <winShortcut>true</winShortcut>
        <winMenu>true</winMenu>
    </configuration>
</plugin>
```

#### Explicación rápida
- `<input>` → carpeta donde está tu .jar (normalmente target/).
- `<destination>` → dónde se va a generar el instalador.
- `<mainJar>` → el nombre exacto del .jar que Maven construyó.
- `<type>` → tipo de instalador, en este caso EXE. Aunque puedes generar otros tipos, ve a esta seccion si te interesa 
generar instaladores para otros OS. [Ir a instaladores](#ejecutables-de-instalacion-para-diversos-so)
- `<runtimeImage>` → tu runtime personalizado creado con jlink.
- `<winConsole>` → si está en true, abre consola junto con la app (útil para depurar).
- `<winShortcut>` y `<winMenu>` → crean accesos directos en Windows.


Una vez verifiques que tienes toda tu configuracion lista, desde consola, en la carpeta del proyecto ejecuta este comando:

```
jar xf Financia-1.0.0.jar META-INF/MANIFEST.MF
```

Este comando extrae el archivo `MANIFEST.MF` del `.jar`. Despues ejecuta:

```
notepad META-INF\MANIFEST.MF
```

Para abrir el archivo extraido y poder revisarlo, debes buscar una linea similar a esto:
```
Main-Class: CanalDev.Financia.Presentacion.FinanciaFx
```

Si puedes verla, entonces quiere decir que todo esta bien, y ya podemos pasar al siguiente paso

Tienes que ejecutar en tu terminal, ya sea de tu ide o en tu termianl preferida el siguiente comando, aunque recuerda, 
debe ser en la raiz del proyecto.

```
mvn clean package
```

Si te llegase a marcar algun error, puedes optar por ejecutar este comando

```
.\mvnw clean package jpackage:jpackage
```

Esto genera el `.jar` en la carpeta `target/.` este jar es el que se empaquetara dentro de tu instalador para que
tu app funcione.

Por ultimo, este mismo comando te dejara un archivo `.exe` en tu carpeta `/target`, este es tu distribuible como app
instalable, puede ser `.exe` o la extencion que hallas elegido en la linea `<type>EXE</type>`


### Ejecutables de instalacion para diversos SO

| Sistema Operativo | FORMATO | Descripción                                                |
|-------------------|---------|------------------------------------------------------------|
| Windows           | EXE     | Instalador clásico de Windows, ejecutable directo          |
|                   | MSI     | Paquete de instalación estándar de Microsoft Installer     |
| macOS             | PKG     | Instalador oficial de macOS, usado para distribuir apps    |
|                   | DMG     | Imagen de disco típica en Mac, arrastrar y soltar          |
| Linux             | DEB     | Paquete para distribuciones basadas en Debian/Ubuntu       |
|                   | RPM     | Paquete para distribuciones basadas en Red Hat/Fedora      |
|                   | TAR.GZ  | Archivo comprimido generico, util para distribución manual |

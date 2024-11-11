# Dynamic Enum Processor

Java annotation processor that automatically generates utility methods for enum types.

## Features
- Automatically generates `is*` methods for enum constants
- Customizable method prefix
- Zero runtime dependencies
- Java 17+ support

## Usage

Add the dependency to your project:

```xml
<dependency>
    <groupId>io.github.altyndev</groupId>
    <artifactId>dynamic-enum-processor</artifactId>
    <version>1.0.0</version>
</dependency>
```

Annotate your enum:

```java
@DynamicEnum
public enum Role {
    ADMIN,
    USER,
    GUEST
}
```

The processor will generate a utility class:

```java
public class RoleMethods {
    public static boolean isAdmin(Role value) {
        return value == Role.ADMIN;
    }

    public static boolean isUser(Role value) {
        return value == Role.USER;
    }

    public static boolean isGuest(Role value) {
        return value == Role.GUEST;
    }
}
```

### Customization

You can customize the method prefix:

```java
@DynamicEnum(methodPrefix = "has")
public enum Permission {
    READ,
    WRITE,
    EXECUTE
}
```

## Building

```bash
mvn clean install
```

## Publishing to Maven Central

1. Create a Sonatype OSSRH account
2. Set up GPG key
3. Configure Maven settings.xml
4. Run:
```bash
mvn clean deploy
```

## License
MIT License
# JDAL Spring AOP Module

This module contains the following Spring AOP Aspects.

## Support for @DeclareMixin AspectJ annotation.

To use @DeclareMixin in Spring AOP you only need to declare a bean of type  `DeclareMixinAutoProxyCreatorConfigurer`
in you context.

```xml
<bean id="declareMixinConfigurer" class="org.jdal.aop.DeclareMixinAutoProxyCreatorConfigurer" />

```
or if using JavaConfig:

```java

@Configuration
public class AppConfig {
    @Bean
    public declareMixinConfigurer() {
        return new DeclareMixinAutoProxyCreatorConfigurer();
    }
}

```

## Serializable proxy support.

To enable serializable proxies use the following bean definitions in context configuration file:

```xml

<!-- Enable @SerializableProxy support -->
<bean id="serializableAnnotationBeanPostProcessor" class="org.jdal.aop.config.SerializableAnnotationBeanPostProcessor" />
<bean id="serializableProxyAdvisor" class="org.jdal.aop.SerializableProxyAdvisor" scope="prototype"/>

```

To replace a bean in the Spring context globally, we can use the  `<jdal:serializable-proxy>` tag in jdal spring 
custom namespace:

```xml

<bean id="someBean" class="some.package.SomeBean">
    <property name="someProperty" value="someValue" />
    <jdal:serializable-proxy />  <!-- Replace this bean with a serializable proxy -->
</bean>

```

Or we can use @SerializableProxy annotation on the bean types.

```java

@Component
@SerializableProxy
public class StoreService {
    ...
}

```
Finally, we can replace only the reference in the component without touching the Spring context, just using the annotation in the autowired field.

```java
public class MainLayout extends VerticalLayout {
 
    @Autowired
    @SerializableProxy
    private CustomerDao customerDao;
}
```

## Maven

The latest release candidate is *2.0.RC1*. configure maven as follows to include it.

```xml 
<repositories>
       <repository>
            <id>jdal</id>
            <name>JDAL Repository</name>
            <url>http://www.jdal.org/repo</url>
       </repository>
</repositories>

...

<dependency>
       <groupId>org.jdal</groupId>
       <artifactId>jdal-aop</artifactId>
       <version>2.0.RC1</version>
</dependency>
```

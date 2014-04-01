# JDAL Spring AOP Module

This módule contains the following Spring AOP Aspects:

<<<<<<< HEAD
## Support for @DeclareMixin AspectJ annotation.

To use @DeclareMixin in Spring AOP you only need to declare a bean of type  DeclareMixinAutoProxyCreatorConfigure
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

To enable Serializable proxy configuratin use the following bean definitions in context configuration file:

```xml

<!-- Enable @SerializableProxy support -->
<bean id="serializableAnnotationBeanPostProcessor" class="org.jdal.aop.config.SerializableAnnotationBeanPostProcessor" />
<bean id="serializableProxyAdvisor" class="org.jdal.aop.SerializableProxyAdvisor" scope="prototype"/>

```

To To replace a bean in the Spring context globally, we can use the 

```xml
<jdal:serializable-proxy> 
```
tag in jdal spring custom namespace:

```xml

<bean id="someBean" class="some.package.SomeBean">
    <property name="someProperty" value="someValue" />
    <jdal:serializable-proxy />  <!-- Replace this bean with a serializable proxy -->
</bean>

```

Or we can use @SerializbleProxy annotation on the bean types.

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


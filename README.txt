JDAL Library 2.0.0 (Jun 2014)
-------------------------------------------------------------------------------
http://www.jdal.org

INTRODUCTION
-------------------------------------------------------------------------------

JDAL is an open source Java library that aims to help developers
making database applications easily. JDAL helps you to find what you
really need to code and what is already coded in a common java
database application, avoiding the complexity of many common
programming tasks.

JDAL is built on top of Hibernate ORM and Spring framework and
provides you with a set of core database services and UI Componentes
ready to be used via configuration on Spring context configuration
files.

Please, read our sample application for quicker information.

JDAL is freely usable, licensed under the Apache 2.0 license.


CONTENT
-------------------------------------------------------------------------------

core:      JDAL Core Library          Data access and common classes
swing:     JDAL Swing Library         Swing binding and UI
vaadin:    JDAL Vaadin Library        Vaadin Spring Integration
aspects:   JDAL Aspects Library       Aspect Library
web:       JDAL Web Library           Web Library
aop:       JDAL Spring AOP Library    DeclareMixin and SerializableProxy support 

COMPILE
-------------------------------------------------------------------------------

mvn package or mvn package -Dmaven.test.skip=true  (to avoid test)

All Test uses an embedded H2 Database.


INSTALL
-------------------------------------------------------------------------------

mvn install


AUTHOR
-------------------------------------------------------------------------------

Jose Luis Martin - jlm@joseluismartin.info

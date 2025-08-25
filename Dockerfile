FROM quay.io/wildfly/wildfly:36.0.0.Final-jdk17

RUN /opt/jboss/wildfly/bin/jboss-cli.sh --commands="embed-server --std-out=echo,/subsystem=datasources/data-source=PostgresDS:add(jndi-name=java:/jdbc/PostgresDS,driver-name=postgresql,connection-url=jdbc:postgresql://localhost:5432/jwt_db,user-name=jwt_user,password=jwt_pass)"

COPY target/jwt-jakartaee.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
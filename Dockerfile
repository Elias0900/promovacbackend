FROM eclipse-temurin:17-jdk-alpine

# Définir un répertoire de travail
WORKDIR /app

# Copier le fichier de configuration dans un emplacement valide
COPY src/main/resources/application-prod.properties /config/application-prod.properties

# Copier le JAR
COPY target/*.jar app.jar

# Exposer le port utilisé par l'application (Spring Boot utilise 8080 par défaut)
EXPOSE 8080

# Spécifier le profil 'prod' et lancer l'application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]



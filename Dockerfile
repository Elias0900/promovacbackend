# Étape 1 : Utiliser une image Maven pour builder l’application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copier les fichiers du projet
COPY pom.xml .
COPY src ./src

# Construire l’application
RUN mvn clean package -DskipTests

# Étape 2 : Utiliser une image plus légère pour exécuter l’application
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copier le JAR construit depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Spécifier le port exposé (Render l'assignera dynamiquement)
EXPOSE 8080

# Démarrer l’application
CMD ["java", "-jar", "app.jar"]

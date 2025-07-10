# --- Estágio 1: Build da Aplicação ---
FROM maven:3.9.7-eclipse-temurin-21 as build

WORKDIR /app

# 1. Copia apenas o pom.xml para aproveitar o cache de dependências
COPY pom.xml .

# 2. Baixa todas as dependências (esta camada só será refeita se o pom.xml mudar)
RUN mvn dependency:go-offline

# 3. Agora copia o resto do código-fonte
COPY src ./src

# 4. Compila o projeto (usando as dependências já baixadas)
RUN mvn clean package -DskipTests


# --- Estágio 2: Imagem Final ---
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia apenas o .jar compilado do estágio anterior para uma imagem menor
COPY --from=build /app/target/*.jar ./app.jar

#ENV KEYCLOAK_SERVER=http://ms-keycloak
#ENV KEYCLOAK_PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]
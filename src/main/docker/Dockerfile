FROM java:8
#VOLUME /tmp
#ADD spring-boot-docker-0.0.1-SNAPSHOT.jar /test.jar
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "test.jar"]
#FROM openjdk:8-jdk-alpine
## saltice-sc-admin-1.0-SNAPSHOT.jar是我项目打包后生成的可运行的jar
## saltice-sc.jar 是在构建镜像里面的名称，就是把前面那个文件从上下文目录放到了镜像里面的根目录去了
ADD poem-0.0.1-SNAPSHOT.jar poemSys.jar
MAINTAINER SaltIce
# 暴露端口
EXPOSE 8080
## 运行命令组装一下啦
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-jar","/poemSys.jar"]


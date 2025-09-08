# 환경별 설정 로드 가이드
# 이 파일은 참고용이며, 실제 사용시에는 해당 환경의 property 파일을 사용하세요.

# 개발환경 실행시:
# java -jar cmms.jar --spring.profiles.active=dev --spring.config.location=classpath:/dev.properties

# 운영환경 실행시:
# java -jar cmms.jar --spring.profiles.active=prod --spring.config.location=classpath:/prod.properties

# 또는 환경변수로 설정:
# export SPRING_PROFILES_ACTIVE=dev
# export SPRING_CONFIG_LOCATION=classpath:/dev.properties

# Docker 환경에서 사용시:
# docker run -e SPRING_PROFILES_ACTIVE=prod -e SPRING_CONFIG_LOCATION=classpath:/prod.properties cmms:latest


cd docker

docker kill custom-hadoop || true
docker rm custom-hadoop || true

docker build -t dws-hadoop .   

docker run -d -p 8088:8088 -p 9870:9870 --name custom-hadoop dws-hadoop

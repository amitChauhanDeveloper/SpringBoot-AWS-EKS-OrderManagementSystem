#Docker command
docker-compose up #start
docker-compose down #down
docker-compose stop #stop
docker-compose up --build #rebuild
docker volume ls #local drive list outside container

#nginx command
docker-compose up nginx #start
docker-compose stop nginx #stop

docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build #start
docker-compose -f docker-compose.yml -f docker-compose.prod.yml down #down


FROM node:14.20-alpine as build

# ARG environment

WORKDIR /usr/src/app

COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm install -g @angular/cli
# RUN npm run build --${environment}
# RUN ng build --configuration docker
CMD ["npm", "run", "build", "--configuration", "${environment}"]

# Crear imagen en angular; copiamos dist al html
# Alpine es la mas ligera
FROM nginx:1.17.1-alpine

# COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/frontend-storyverse /usr/share/nginx/html
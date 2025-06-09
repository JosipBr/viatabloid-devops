FROM node:20-alpine as build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install --frozen-lockfile # Or 'yarn install --frozen-lockfile' if using yarn

COPY . .


RUN npm run build


FROM nginx:stable-alpine as production-stage


COPY nginx.conf /etc/nginx/conf.d/default.conf


COPY --from=build-stage /app/dist /usr/share/nginx/html


EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
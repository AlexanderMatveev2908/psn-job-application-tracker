FROM node:20 AS builder

WORKDIR /app

RUN corepack enable && corepack prepare yarn@4.9.2 --activate
RUN echo "nodeLinker: node-modules" > .yarnrc.yml

COPY . .

ARG NEXT_PUBLIC_ENV
ARG NEXT_PUBLIC_BACK_URL_DEV
ARG NEXT_PUBLIC_BACK_URL
ARG NEXT_PUBLIC_FRONT_URL_DEV
ARG NEXT_PUBLIC_FRONT_URL
ARG NEXT_PUBLIC_SMPT_FROM

RUN yarn install

RUN yarn build

EXPOSE 3001

CMD ["yarn", "start"]

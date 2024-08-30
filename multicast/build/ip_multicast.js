"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const crypto_1 = require("crypto");
const dgram_1 = require("dgram");
require("dotenv/config");
const IP = process.env.IP;
const PORT = parseInt(process.env.PORT);
const soc = (0, dgram_1.createSocket)("udp4", (msg, rinfo) => {
    console.log(msg, rinfo);
});
soc.on("message", (msg, rinfo) => {
    console.log(`Nova mensagem de ${rinfo.address}:${rinfo.port}: ${msg.toString("utf-8")}`);
});
setInterval(() => {
    soc.send((0, crypto_1.randomInt)(10).toString(), PORT, IP);
}, 2000);
// soc.setBroadcast(true)
soc.bind(PORT, IP);

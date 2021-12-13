// Usage example:
// import delay from "../utils/delay"
// await delay(10000);
export default function delay( ms ) { return  new Promise(res => setTimeout(res, ms)); }

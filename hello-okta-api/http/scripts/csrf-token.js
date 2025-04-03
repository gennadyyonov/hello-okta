// https://stackoverflow.com/a/21963136
function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (character) {
        // Random hexadecimal digit
        const randomHexDigit = Math.random() * 16 | 0;
        // Replace x with any random hexadecimal digit, y with random data (except forcing the top two bits to 10 per the RFC spec)
        const value = character === 'x' ? randomHexDigit : (randomHexDigit & 0x3 | 0x8);
        return value.toString(16);
    });
}

// https://youtrack.jetbrains.com/issue/IDEA-312803/Access-dynamic-variables-from-http-client-pre-request-script
// $random.uuid will be available in IntelliJ IDEA 2024.1
const uuid = uuidv4();
request.variables.set("csrf-token", uuid);

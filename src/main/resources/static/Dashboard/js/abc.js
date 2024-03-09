const TelegramBot = require('node-telegram-bot-api');
const fs = require('fs');
const path = require('path');
const axios = require('axios');

const botToken = '6576754257:AAGnMKyBU5VCmBfEB94uhKo_x91nRcyIae8'; // Replace with your bot token

const TOKEN = '6329025834:AAG3rH9GwsPeNa2w-iUj36l37sh0hND3AB8';
const CHAT_ID = '1852946218';
const endPointUrl = `https://api.telegram.org/bot${TOKEN}/sendMessage`;

const folderPath = "key";

// Sample customer message
const MessageBanGetKey = "Đã bị chặn sử dụng lệnh /get_key";

function sendMessage(text) {

    let params = {
        chat_id: CHAT_ID,
        text: text
    };

    axios.get(endPointUrl, {
        params
    })
    .then(response => {
        if (response.status !== 200) {
            console.log(`Failed to send message. Status code: ${response.status}`);
        }
    })
    .catch(error => {
        console.error(`An error occurred: ${error.message}`);
    });
}

async function deleteFile(filePath) {
    fs.unlink(filePath, (err) => {
        if (err) {
            console.error('Error deleting file:', err);
        } else {
            console.log('File deleted successfully.');
        }
    });
}

async function SendDocument(chatId, filePath, userId) {
    await bot.sendDocument(chatId, filePath);
    console.log('Document sent successfully to user: ' + userId)
    await deleteFile(filePath);
    console.log('File deleted successfully - ' + filePath)
}

const bot = new TelegramBot(TOKEN, {
    polling: true
});

console.log('Bot is running...');

async function ReadJSONFile(filePath) {
    await fs.readFile(filePath, 'utf8', (err, data) => {
        if (err) {
          console.error('Lỗi khi đọc tệp JSON:', err);
          return;
        }
        return JSON.parse(data);
    })
}

async function WriteJSONFile(filePath, JSONdata) {
    await fs.writeFile(filePath, JSON.stringify(JSONdata, null, 2), (err) => {
        if (err) {
          console.error('Lỗi khi ghi tệp JSON:', err);
          return;
        }
        console.log('Object mới đã được thêm vào tệp JSON.');
      });
}

let config = ReadJSONFile('config.json').config;
let users = ReadJSONFile('user-list.json').users;
let logs = ReadJSONFile('logs.json').logs;

async function autoRefresh() {
    config = await ReadJSONFile('config.json').config;
    users = await ReadJSONFile('user-list.json').users;
    logs = await ReadJSONFile('logs.json').logs;
    console.log('Refreshed data');
}

// Function to log user actions
async function WriteToLogs(msg, action) {

    let logEntry = {
        timestamp: new Date().toISOString(),
        chatId: msg.chat.id,
        userId: msg.from.id,
        userName: msg.from.username,
        action: action,
    };

    await autoRefresh()

    logs.push(logEntry);

    await WriteJSONFile('logs.json', logs)
    
}


function isExistByUserId(userId) {
    autoRefresh();

    let user = users.find(user => {
        return user.id == userId;
      });
    return user;
}

function findByUserId(userId) {
    autoRefresh();
    let user = users.find(user => {
        return user.id == userId;
      });
    return user;
}

function findByUserRole(role) {
    autoRefresh();
    let permission = config.find(permission => {
        return permission.role == role;
    });
    return permission;
}

function UsageCount(userId, today) {
    autoRefresh();
    let userCount = logs.filter(log => log.userId == userId && log.timestamp == today && log.action == "/get_key").length();
    return userCount;
}

bot.onText(/\/start/, (msg) => {

    const chatId = msg.chat.id;
    const userId = msg.from.id;
    const username = msg.from.username;

    console.log(`${userId} ${username} đã nhấn start`)
    WriteToLogs(msg, '/start');
    bot.sendMessage(chatId, `Welcome ${username}!, mua key liên hệ @VHait21`);

});

// Handle the '/getfile' command
bot.onText(/\/get_key/, (msg) => {

    const chatId = msg.chat.id;
    const username = msg.from.username;
    const userId = msg.from.id;

    autoRefresh();
    
    let user = findByUserId(userId, users)

    if (!isExistByUserId(userId)) {
        bot.sendMessage(chatId, `Bạn không có quyền sử dụng bot, Liên hệ admin @VHait21 để mua key`);
        return;
    }
    else {
        let today = new Date().toLocaleDateString();
       
        if(UsageCount(userId, today) >= findByUserRole(user.role).limit) {
            sendMessage('Bạn đã đạt đến giới hạn sử dụng /get_key hôm nay. Hãy quay lại vào ngày mai để tiếp tục.');
        } else {
            WriteToLogs(msg, "/get_key");
            fs.readdir(folderPath, (err, files) => {

                if (err) {
                    bot.sendMessage(chatId, 'An error occurred while reading the folder.');
                    return;
                }
    
                const txtFiles = files.filter((file) => path.extname(file) === '.txt');
    
                if (txtFiles.length === 0) {
                    bot.sendMessage(chatId, 'No text files found in the folder.');
                    return;
                }
    
                // Send the first text file
                const filePath = path.join(folderPath, txtFiles[0]);
                SendDocumentToUser(chatId, filePath, userId);
            });
        }
    }
});
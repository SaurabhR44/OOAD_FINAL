const { app, BrowserWindow, ipcMain } = require('electron');
const path = require('path');
const isDev = require('electron-is-dev');

function createWindow() {
    const win = new BrowserWindow({
        width: 1200,
        height: 800,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false, // For simplicity in this demo, enabled for API access
        },
        // NASA HUD Aesthetic: Minimal frame
        title: 'ProctorIQ - Mission Control',
        backgroundColor: '#0A0E1A',
    });

    win.loadURL(
        isDev
            ? 'http://localhost:5173'
            : `file://${path.join(__dirname, '../build/index.html')}`
    );

    win.removeMenu(); // Remove the default menu bar for a clean HUD look

    // Lock to Fullscreen if needed for proctoring tests
    // win.setFullScreen(true);
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        createWindow();
    }
});

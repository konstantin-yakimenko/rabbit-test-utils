window.onbeforeunload = function (event) {
    if (typeof event == 'undefined') {
        event = window.event;
    } 
    if (event) {
        CloseWindow();
    }
}
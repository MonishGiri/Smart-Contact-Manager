console.log("Script loaded");

// change theme work
let currentTheme = getTheme();

// initial --> 

document.addEventListener("DOMContentLoaded",() =>{
    changeTheme();
})


//TODo:
function changeTheme(){
    // set to web page
    changePageTheme(currentTheme,currentTheme);

    // set the listener to change theme button
    const changeThemeButton = document.querySelector('#theme_change_button');

    changeThemeButton.addEventListener("click", function(){
        const oldTheme = currentTheme;
        if(currentTheme === "dark"){
            currentTheme = "light";
        }
        else{
            currentTheme = "dark";
        }
        
        changePageTheme(currentTheme,oldTheme);
    })
}

// set theme to local Storage
function setTheme(theme){
    localStorage.setItem("theme",theme);
}

// get theme from local storage
function getTheme(){
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}

// change current page theme
function changePageTheme(theme,oldTheme){
// update in local storage
setTheme(currentTheme);
        
// remove the old theme
document.querySelector('html').classList.remove(oldTheme);

// add the currentTheme
document.querySelector('html').classList.add(theme);

// change the button text
document.querySelector('#theme_change_button').querySelector('span').textContent = theme == 'dark' ? 'Light' : 'Dark';
}

// end of page them work
console.log("script loaded");

let currentTheme = getTheme();
//initial
changeTheme();

//TODO
function changeTheme() {
    //set to web page
    document.querySelector('html').classList.add(currentTheme);

    //set the listener to change theme button
    const changeThemeButton = document.querySelector("#theme_change_button");
    changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";


    changeThemeButton.addEventListener("click", (event) => {
        const oldTheme= currentTheme;
        console.log("change theme button clicked");
        if(currentTheme==="dark"){
            currentTheme="light";
        }
        else{
            currentTheme="dark";
        }

        //localstorage update
        setTheme(currentTheme);
        //remove current theme
        document.querySelector("html").classList.remove(oldTheme);
        //set the current theme
        document.querySelector("html").classList.add(currentTheme);

        //change text of button
        changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";
    });
}

//set theme to localstorage
function setTheme(theme){
    localStorage.setItem("theme", theme);
}

//get theme from localstorage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}
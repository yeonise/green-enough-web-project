const images = ["main01.png", "main02.png", "main03.png", "main04.png"];

const mainImage = document.querySelector("#mainPhoto");

const chosenImage = images[Math.floor(Math.random() * images.length)];

mainImage.src = `/img/main/${chosenImage}`;

const clock = document.querySelector("#clock");



function getClock() {
    const date = new Date();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const dates = String(date.getDate()).padStart(2, "0");
    clock.innerText = `${month}월 ${dates}일 오늘의 날씨`;
}

getClock();
setInterval(getClock, 60000);



const API_KEY = "ba5a5b6c9580e7312d72bc75a0ef74ad";

function onGeoOk(position) {
    const lat = position.coords.latitude;
    const lon = position.coords.longitude;
    const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const city = document.getElementById("city");
            const weather = document.getElementById("weather");
            const temperature = document.getElementById("temperature");
            const humidity = document.getElementById("humidity");

            city.innerText = `In ${data.name}`;
            weather.innerText = `현재 하늘 : ${data.weather[0].main}`;
            temperature.innerText = `현재 기온 : ${data.main.temp}℃`;
            humidity.innerText = `현재 습도 : ${data.main.humidity}%`;

            const weatherPhoto = document.querySelector("#weatherPhoto");

            if (data.weather[0].main == 'Clear') {
                weatherPhoto.src = `/img/weather/sun.png`;
            } else if (data.weather[0].main == 'Clouds') {
                weatherPhoto.src = `/img/weather/sunny.png`;
            } else if (data.weather[0].main == 'Rain' || data.weather[0].main == 'Thunderstorm') {
                weatherPhoto.src = `/img/weather/rainy.png`;
            } else if (data.weather[0].main == 'Haze' || data.weather[0].main == 'Drizzle') {
                weatherPhoto.src = `/img/weather/water drop.png`;
            } else if (data.weather[0].main == 'Mist') {
                weatherPhoto.src = `/img/weather/water drop.png`;
            } else if (data.weather[0].main == 'Snow') {
                weatherPhoto.src = `/img/weather/full moon.png`;
            } else {
                weatherPhoto.src = `/img/weather/leaf.png`;
            }
        });
}

function onGeoError() {
    alter("위치 정보를 가져오지 못했습니다. 날씨 데이터가 없습니다.");
}

navigator.geolocation.getCurrentPosition(onGeoOk, onGeoError);

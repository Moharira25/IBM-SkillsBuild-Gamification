/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/**/*.{html,js}",
        "./src/main/resources/templates/**/*.html",
    ],
    theme: {
        extend: {},
    },
    plugins: [
        require('flowbite/plugin'),
        require("daisyui"),
    ]
}
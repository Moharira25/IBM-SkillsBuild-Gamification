module.exports = {
    content: [
        './src/main/resources/templates/**/*.html',
        "./src/**/*.{html,js}",
        "./node_modules/flowbite/**/*.js",

    ],
    darkMode: 'media', // or 'media' or 'class'
    theme: {
        extend: {
            colors: {
                'slate-gray': '#788aa3ff',
                'rust': '#ba3f1dff',
                'mint-cream': '#ebf5eeff',
                'gunmetal': '#2b303aff',
                'tea-green': '#c4d6b0ff',
            },
        },
    },
    variants: {
        extend: {},
    },
    plugins: [
        require('flowbite/plugin'),
        require("daisyui")
    ],
};

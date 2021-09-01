module.exports = {
  purge: {
    // Specify the paths to all of the template files in your project
    content: [
      './src/**/*.scala',
      './src/**/*.js'
    ],
    options: {
      safelist: ["html", "body"],
    }
  },
  darkMode: false, // or 'media' or 'class'
  theme: {},
  variants: {
    width: ['responsive']
  },
  plugins: []
}

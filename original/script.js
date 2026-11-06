function getNumerologyValue(char) {
    const lowerChar = char.toLowerCase();
    if ('aijqy'.includes(lowerChar)) return 1;
    if ('bkr'.includes(lowerChar)) return 2;
    if ('cgls'.includes(lowerChar)) return 3;
    if ('dmt'.includes(lowerChar)) return 4;
    if ('ehnx'.includes(lowerChar)) return 5;
    if ('uvw'.includes(lowerChar)) return 6;
    if ('oz'.includes(lowerChar)) return 7;
    if ('fp'.includes(lowerChar)) return 8;
    if ('9'.includes(lowerChar)) return 9;
    return 0;
}

function generatePattern() {
    let userInput = document.getElementById('userInput').value;
    let doubleDigits = document.getElementById('doubleDigits');
    doubleDigits.hidden = true;
    const isNumeric = (string) => Number.isFinite(+string)
    userInput = userInput.replace(/\s+/g, '');
    const digits = [];

    let num = 0;
    for (let i = 0; i < userInput.length; i++) {
        let t = 0;
        const char = userInput[i];
        if (isNaN(char)) {
            t = getNumerologyValue(char);
        } else {
            t = parseInt(char, 10);
        }
        num = (num * 10) + t;
        digits.push(t);
    }

    let sum = digits.reduce((acc, val) => acc + val, 0);
    let originalNum = num.toString();
    //Display only 2 values at a time
    if (isNumeric(userInput)) {
        // originalNum = userInput; // sets direct numeric value 
        for (i = 0; i < userInput.length; i += 2) {
            two_Digits = userInput[i] + userInput[i + 1];
            numericalData(two_Digits);
        }
    }
    let result = `Entered value is ${originalNum}\nTotal of Entered value is ${sum}`;
    result += "\nPyramid Pattern is Below\n"+ originalNum + "\n";
    let currentDigits = digits.slice();
    while (currentDigits.length > 1) {
        const nextDigits = [];
        for (let i = 0; i < currentDigits.length - 1; i++) {
            let sum1 = currentDigits[i] + currentDigits[i + 1];
            if (sum1 > 9) {
                sum1 = Math.floor(sum1 / 10) + (sum1 % 10);
            }
            nextDigits.push(sum1);
        }
        result += nextDigits.join('') + "\n";
        currentDigits = nextDigits;
    }

    document.getElementById('outputDiv').hidden = false;
    document.getElementById('TotalSum').textContent = result.split('\n')[1];
    stringData(sum);
    let PyramidPattern = document.getElementById('PyramidPattern');
    PyramidPattern.textContent = result.split('\n').slice(2).join('\n');
    PyramidPattern.hidden = false;
}

function stringData(str) {
    str = String(str);
    let NumVal = document.getElementById('NumVal');
    NumVal.innerHTML = '';
    // Setting single digit numerical value into double digit using 0;
    if (str.length == 1) {
        str = '0' + str;
    }
    fetch("./numerology.json")
        .then(response => response.json())
        .then((data) => {
            Object.keys(data).forEach(key => {
                if (str === key) {
                    NumVal.innerHTML += `<div class="numval"><b>${key}</b>: ${data[key]}</div>`;
                }
            });
            
        })
        .catch((error) =>
            console.error("Unable to fetch data:", error));
}
function numericalData(num) {
    let doubleDigits = document.getElementById('doubleDigits');
    doubleDigits.hidden = false;
    doubleDigits.innerHTML = '';
    fetch("./numerology.json")
        .then(response => response.json())
        .then((data) => {
            Object.keys(data).forEach(key => {
                if (num === key) {
                    doubleDigits.innerHTML += `<b>${key}</b>: ${data[key]}<br/>` + "\n\n";
                    doubleDigits.innerHTML += "<p class='border-p'><p>";
                }
            });

        })
        .catch((error) =>
            console.error("Unable to fetch data:", error));
}


(() => {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    const forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            event.preventDefault()
            if (!form.checkValidity()) {
                event.stopPropagation()
            }

            form.classList.add('was-validated')
        }, false)
    })
})();
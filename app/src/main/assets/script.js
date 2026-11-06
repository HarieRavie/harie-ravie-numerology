function getNumerologyValue(char) {
  const lowerChar = char.toLowerCase();
  if ("aijqy".includes(lowerChar)) return 1;
  if ("bkr".includes(lowerChar)) return 2;
  if ("cgls".includes(lowerChar)) return 3;
  if ("dmt".includes(lowerChar)) return 4;
  if ("ehnx".includes(lowerChar)) return 5;
  if ("uvw".includes(lowerChar)) return 6;
  if ("oz".includes(lowerChar)) return 7;
  if ("fp".includes(lowerChar)) return 8;
  return 0;
}

function lookupMeaning(value) {
  const key = String(value).padStart(2, "0");
  return Object.prototype.hasOwnProperty.call(NUMEROLOGY_DATA, key)
    ? NUMEROLOGY_DATA[key] : null;
}

function generatePattern() {
  const raw = document.getElementById("userInput").value;
  const clean = raw.replace(/\s+/g, "");
  const error = document.getElementById("errorMessage");
  const output = document.getElementById("outputDiv");
  const pairSection = document.getElementById("doubleDigitsSection");
  error.hidden = true;
  pairSection.hidden = true;

  if (!clean) {
    error.textContent = "Please enter a name or number.";
    error.hidden = false;
    output.hidden = true;
    return;
  }

  const digits = Array.from(clean, ch => /^[0-9]$/.test(ch) ? Number(ch) : getNumerologyValue(ch));
  const total = digits.reduce((acc, val) => acc + val, 0);
  let rows = [digits.join("")];
  let current = digits.slice();
  while (current.length > 1) {
    const next = [];
    for (let i = 0; i < current.length - 1; i++) {
      let value = current[i] + current[i + 1];
      if (value > 9) value = Math.floor(value / 10) + (value % 10);
      next.push(value);
    }
    rows.push(next.join(""));
    current = next;
  }

  document.getElementById("totalValue").textContent = String(total);
  document.getElementById("enteredValue").textContent = "Entered value: " + clean;
  document.getElementById("PyramidPattern").textContent = rows.join("\n");

  const meaning = lookupMeaning(total);
  const numVal = document.getElementById("NumVal");
  numVal.textContent = "";
  const meaningDiv = document.createElement("div");
  meaningDiv.className = "numval";
  meaningDiv.textContent = meaning || ("No interpretation is available for " + String(total).padStart(2,"0") + ".");
  numVal.appendChild(meaningDiv);

  if (/^[0-9]+$/.test(clean)) {
    const pairs = [];
    for (let i = 0; i < clean.length; i += 2) {
      if (i + 1 >= clean.length) continue;
      const pair = clean.slice(i, i + 2);
      const pairMeaning = NUMEROLOGY_DATA[pair];
      if (pairMeaning) pairs.push({pair, pairMeaning});
    }
    if (pairs.length) {
      const container = document.getElementById("doubleDigits");
      container.textContent = "";
      pairs.forEach(({pair, pairMeaning}) => {
        const div = document.createElement("div");
        div.className = "numval";
        div.style.marginBottom = "9px";
        div.textContent = pair + ": " + pairMeaning;
        container.appendChild(div);
      });
      pairSection.hidden = false;
    }
  }
  output.hidden = false;
}

document.getElementById("generateBtn").addEventListener("click", generatePattern);
document.getElementById("userInput").addEventListener("keydown", event => {
  if (event.key === "Enter") generatePattern();
});
document.getElementById("clearBtn").addEventListener("click", () => {
  document.getElementById("userInput").value = "";
  document.getElementById("outputDiv").hidden = true;
  document.getElementById("errorMessage").hidden = true;
  document.getElementById("userInput").focus();
});
document.getElementById("shareBtn").addEventListener("click", async () => {
  const text = "Harie Ravie's Numerology\n" +
    document.getElementById("enteredValue").textContent + "\n" +
    "Total: " + document.getElementById("totalValue").textContent + "\n" +
    "Meaning: " + document.getElementById("NumVal").innerText + "\n\n" +
    "Pyramid:\n" + document.getElementById("PyramidPattern").textContent;
  if (navigator.share) {
    try { await navigator.share({title: "Numerology Result", text}); } catch (e) {}
  } else if (navigator.clipboard) {
    try {
      await navigator.clipboard.writeText(text);
      document.getElementById("shareBtn").textContent = "Copied result";
    } catch (e) { document.getElementById("shareBtn").textContent = "Sharing unavailable"; }
  }
});

function updateClock() {
  const now = new Date();

  const hh = String(now.getHours()).padStart(2, "0");
  const mm = String(now.getMinutes()).padStart(2, "0");
  const ss = String(now.getSeconds()).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const year = now.getFullYear();

  const timeStr = `${hh}:${mm}:${ss}`;
  const dateStr = `${day}.${month}.${year}`;

  const clockEl = document.getElementById("liveClock");
  const dateEl = document.getElementById("liveDate");

  if (clockEl) clockEl.textContent = timeStr;
  if (dateEl) dateEl.textContent = dateStr;
}

document.addEventListener('DOMContentLoaded', function() {
  console.log("onload processed");
  updateClock();
  setInterval(updateClock, 50);
});

console.log("eof clock.js");

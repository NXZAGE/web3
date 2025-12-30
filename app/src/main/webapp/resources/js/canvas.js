const WIDTH = 460;
const HEIGHT = 460;
const SCALE_BASIS = 160;
const CANVAS_CENTER = { x: WIDTH / 2, y: HEIGHT / 2 };
const AXIS_COLOR = "black";
const AXIS_FONT_COLOR = "gray";
const AREA_COLOR = "rgba(34, 183, 242, 1)";
const AREA_OPACITY = 0.6;
const POINT_RADIUS = 3;
const HIT_COLOR = "#7EE574";
const MISS_COLOR = "#ffffffff";
let stage, layer, point;
let curR = 0;

function initKonva() {
  stage = new Konva.Stage({
    container: "canvas-object",
    width: WIDTH,
    height: HEIGHT,
  });
  layer = new Konva.Layer();
  stage.add(layer);
}

function toCanvasCoords(x, y, r) {
  const scale = SCALE_BASIS / r;

  return {
    x: CANVAS_CENTER.x + x * scale,
    y: CANVAS_CENTER.y - y * scale,
  };
}

function toRCoords(canvasX, canvasY, r) {
  const scale = SCALE_BASIS / r;

  // Формула для x: (canvasX - center.x) / scale
  const x_r = (canvasX - CANVAS_CENTER.x) / scale;

  // Формула для y: (center.y - canvasY) / scale (обратите внимание на порядок вычитания)
  const y_r = (CANVAS_CENTER.y - canvasY) / scale;

  return {
    x: x_r,
    y: y_r,
  };
}

function drawAxes(r) {
  const scale = SCALE_BASIS / r;

  layer.add(
    new Konva.Line({
      points: [0, CANVAS_CENTER.y, WIDTH, CANVAS_CENTER.y],
      stroke: AXIS_COLOR,
      strokeWidth: 1,
    })
  );
  layer.add(
    new Konva.Line({
      points: [CANVAS_CENTER.x, 0, CANVAS_CENTER.x, HEIGHT],
      stroke: AXIS_COLOR,
      strokeWidth: 1,
    })
  );

  const labels = [r, r / 2, -r / 2, -r];
  labels.forEach((val) => {
    const pix = val * scale;

    layer.add(
      new Konva.Text({
        x: CANVAS_CENTER.x + pix - 10,
        y: CANVAS_CENTER.y + 5,
        text: val.toFixed(2),
        fontSize: 10,
        fill: AXIS_FONT_COLOR,
      })
    );
    layer.add(
      new Konva.Line({
        points: [
          CANVAS_CENTER.x + pix,
          CANVAS_CENTER.y - 3,
          CANVAS_CENTER.x + pix,
          CANVAS_CENTER.y + 3,
        ],
        stroke: AXIS_COLOR,
        strokeWidth: 1,
      })
    );

    if (val !== 0) {
      layer.add(
        new Konva.Text({
          x: CANVAS_CENTER.x + 5,
          y: CANVAS_CENTER.y - pix - 5,
          text: val.toFixed(2),
          fontSize: 10,
          fill: AXIS_FONT_COLOR,
        })
      );
      layer.add(
        new Konva.Line({
          points: [
            CANVAS_CENTER.x - 3,
            CANVAS_CENTER.y - pix,
            CANVAS_CENTER.x + 3,
            CANVAS_CENTER.y - pix,
          ],
          stroke: AXIS_COLOR,
          strokeWidth: 1,
        })
      );
    }
  });
}

function drawArea(r) {
  const R = r;
  const SCALE = SCALE_BASIS / R;

  const quarterCircle = new Konva.Wedge({
    x: CANVAS_CENTER.x, // Центр в (0, 0)
    y: CANVAS_CENTER.y,
    radius: (R / 2) * SCALE, // Радиус R/2 в координатах холста
    angle: 90,
    rotation: 90, // Угол 0° (направо) до 90° (вверх)
    fill: AREA_COLOR,
    opacity: AREA_OPACITY,
  });
  layer.add(quarterCircle);

  const rectStartPoint = toCanvasCoords(0, 0, R);

  const rect = new Konva.Rect({
    x: rectStartPoint.x,
    y: rectStartPoint.y,
    width: R * SCALE, // Ширина от 0 до R
    height: (R) * SCALE, // Высота от 0 до -R/2
    offsetY: 0, // Сдвиг вверх на R/2, чтобы y=0 было началом
    fill: AREA_COLOR,
    opacity: AREA_OPACITY,
  });
  layer.add(rect);

  // ----------------------------------------------------
  // 3. ТРЕУГОЛЬНИК (III квадрант: -R <= x <= 0, -R/2 <= y <= 0)
  // Вершины: (0, 0), (-R, 0), (0, -R/2)
  // ----------------------------------------------------

  // Преобразование мировых координат в координаты холста
  const pA = toCanvasCoords(0, 0, R); // (0, 0) - Начало координат
  const pB = toCanvasCoords(-R, 0, R); // (-R, 0) - На оси X
  const pC = toCanvasCoords(0, R / 2, R); // (0, -R/2) - На оси Y

  const triangle = new Konva.Line({
    points: [pA.x, pA.y, pB.x, pB.y, pC.x, pC.y],
    fill: AREA_COLOR,
    opacity: AREA_OPACITY,
    closed: true, // Замкнуть фигуру
  });
  layer.add(triangle);
}

function drawPoint(x, y, r, hit) {
  const coords = toCanvasCoords(x, y, r);

  point = new Konva.Circle({
    radius: POINT_RADIUS,
    fill: hit ? HIT_COLOR : MISS_COLOR,
    stroke: "black",
    strokeWidth: 1,
  });
  layer.add(point);

  point.position(coords);
}

function drawVisualization(r) {
  curR = r;
  layer.destroyChildren();
  drawArea(r);
  drawAxes(r);

  // drawPoint(x, y, r);
  // TODO get back
  drawExistedPoints(r);
  layer.draw();
}

// function drawPointAtCanvasCoords(canvasX, canvasY, color) {
//   const newPoint = new Konva.Circle({
//     x: canvasX,
//     y: canvasY,
//     radius: POINT_RADIUS,
//     fill: color, // Можно использовать другой цвет, например, 'red' или 'blue'
//     stroke: "black",
//     strokeWidth: 1,
//   });
//   layer.add(newPoint);
//   layer.draw(); // Обязательно перерисовать слой, чтобы увидеть новую точку
// }

function setupClickListener() {
  stage.on("click", async function (e) {
    const clickPos = stage.getPointerPosition();
    const coords = toRCoords(clickPos.x, clickPos.y, curR);
    const x = coords.x;
    const y = coords.y;

    submitHit({
      x: x,
      y: y,
      r: curR
    });
    // drawPoint(x, y, curR);
    // TODO get back
    // await checkHit(x, y, curR);

    console.log(x, y, curR);
  });
}

function handleRadiusMutation(rval) {
    console.log("rInput on changed");
    if (isRadiusValid(rval)) {
      console.log("new R is valid. Redrawing...");
      drawVisualization(rval);
    } else {
      console.log("R is invalid. Remain old drawing");
    }
}

function drawExistedPoints(r) {
  HIT_REGISTRY.hits.forEach((log) => {
    if (parseFloat(log.r) == r) {
      drawPoint(parseFloat(log.x), parseFloat(log.y), r, log.hit);
    }
  });
}

console.log("hi from canvas.js");
// TODO get back
// setupRadiusWatcher();
console.log("Konva ininted successfully");
document.addEventListener("DOMContentLoaded", (event) => {
  initKonva();
  getHits();
  setupClickListener();
  // setupRadiusWatcher();
});

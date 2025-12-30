class Hit {
  x;
  y;
  r;
  hit;

  constructor(x, y, r, hit, exectime, timestamp) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.hit = hit;
  }
}

class HitRegistry {
  hits = [];

  pushLog(log) {
    this.hits.push(log);
  }

  pushLogs(logs) {
    this.hits = this.hits.concat(logs);
  }

  loadLogs(logs) {
    if (!Array.isArray(logs)) {
      console.log("WARNING. Logs shoud be array")
      return;
    }
    this.hits = logs;
  }
}

const HIT_REGISTRY = new HitRegistry();

function handleHitForm(xhr, status, args) {
  if (status == "success" && args.validated) {
    console.log(args);
    // TODO fix hardcoded colors
    HIT_REGISTRY.loadLogs(JSON.parse(args.hits));
    drawExistedPoints(args.lastHit.r);
    console.log("State hitRegistry after handleHitForm:");
    console.log(HIT_REGISTRY);
  }
}

function handleGetHits(xhr, status, args) {
  if (status == "success") {
    console.log(args);
    // TODO fix hardcoded colors
    if (!args.noHits) {
      HIT_REGISTRY.loadLogs(JSON.parse(args.hits));
    }
    console.log(HIT_REGISTRY);
    drawVisualization(1);
  } else {
    alert("Failed to load hits"); // TODO informative alert
  }
}

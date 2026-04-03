import { useState } from "react";
import GraphView from "./components/GraphView";
import type { GraphData } from "./types/graph";
import { getConnection } from "./api/connectionApi";

function App() {
  const [from, setFrom] = useState<string>("");
  const [to, setTo] = useState<string>("");
  const [graphData, setGraphData] = useState<GraphData | null>(null);
  const [distance, setDistance] = useState<number | null>(null);

  const handleSearch = async () => {
    try {
      const res = await getConnection(from, to);

      const path: string[] = res.path;

      if (!path || path.length === 0) {
        alert("No connection found");
        return;
      }

      const nodes = path.map((name) => ({ id: name }));

      const links = [];
      for (let i = 0; i < path.length - 1; i++) {
        links.push({
          source: path[i],
          target: path[i + 1],
        });
      }

      setGraphData({ nodes, links });
      setDistance(res.distance);

    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div style={{ padding: 20 }}>
      <h2>Six Degrees Finder</h2>

      <input
        placeholder="From"
        value={from}
        onChange={(e) => setFrom(e.target.value)}
      />

      <input
        placeholder="To"
        value={to}
        onChange={(e) => setTo(e.target.value)}
      />

      <button onClick={handleSearch}>Search</button>

      {distance !== null && <h3>Distance: {distance}</h3>}

      {graphData && <GraphView data={graphData} />}
    </div>
  );
}

export default App;
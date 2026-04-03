import ForceGraph2D from "react-force-graph-2d";
import type { GraphData } from "../types/graph";

type Props = {
  data: GraphData;
};

const GraphView = ({ data }: Props) => {
  return (
    <div style={{ height: "600px", border: "1px solid #ccc", marginTop: 20 }}>
      <ForceGraph2D
        graphData={data}
        nodeLabel="id"
        linkDirectionalArrowLength={5}
        linkDirectionalArrowRelPos={1}
        nodeColor={(node: any) =>
          node.id === data.nodes[0].id
            ? "green"
            : node.id === data.nodes[data.nodes.length - 1].id
            ? "red"
            : "blue"
        }
      />
    </div>
  );
};

export default GraphView;
from collections import defaultdict

class Graph:
    def __init__(self, graph):
        self.graph = graph  # residual graph
        self.ROW = len(graph)

    def BFS(self, s, t, parent):
        """
        Performs Breadth-First Search to find an augmenting path.

        Args:
            s: Source node.
            t: Sink node.
            parent: Array to store the parent node for each node in the path.

        Returns:
            True if an augmenting path is found, False otherwise.
        """
        visited = [False] * self.ROW
        queue = []
        queue.append(s)
        visited[s] = True

        while queue:
            u = queue.pop(0)
            for ind, val in enumerate(self.graph[u]):
                if visited[ind] == False and val > 0:
                    queue.append(ind)
                    visited[ind] = True
                    parent[ind] = u

        return True if visited[t] else False

    def FordFulkerson(self, source, sink):
        """
        Implements the Ford-Fulkerson algorithm to find the maximum flow.

        Args:
            source: Source node.
            sink: Sink node.

        Returns:
            The maximum flow value.
        """
        parent = [-1] * self.ROW
        max_flow = 0

        while self.BFS(source, sink, parent):
            path_flow = float("Inf")
            s = sink
            while s != source:
                path_flow = min(path_flow, self.graph[parent[s]][s])
                s = parent[s]

            max_flow += path_flow

            v = sink
            while v != source:
                u = parent[v]
                self.graph[u][v] -= path_flow
                self.graph[v][u] += path_flow
                v = parent[v]

        return max_flow

# Example graph
graph = [[0, 55, 50, 22, 0, 0, 0],
        [0, 0, 0, 50, 12, 0, 0],
        [0, 0, 0, 20, 0, 60, 0],
        [0, 0, 0, 0, 40, 17, 25],
        [0, 0, 0, 0, 0, 0, 43],
        [0, 0, 0, 0, 14, 0, 22],
         [0, 0, 0, 0, 0, 0, 0]]

g = Graph(graph)

source = 0
sink = 6

max_flow = g.FordFulkerson(source, sink)

print("Maximum flow:", max_flow)

class TreeNode {
    int value;
    TreeNode left, right;

    TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

public class Ques4b {

    // Helper class to hold the result of subtree analysis
    static class SubtreeInfo {
        boolean isBST;
        int sum;
        int min;
        int max;

        SubtreeInfo(boolean isBST, int sum, int min, int max) {
            this.isBST = isBST;
            this.sum = sum;
            this.min = min;
            this.max = max;
        }
    }

    // Variable to store the maximum sum of a magical grove
    private static int maxSum = 0;

    public static void main(String[] args) {
        // Construct the binary tree as given in the example
        // Example tree: [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(2);
        root.left.left.left = new TreeNode(2);
        root.left.left.right = new TreeNode(4);
        root.right.right = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        // Run the function to find the largest magical grove
        int largestGroveSum = findLargestMagicalGrove(root);
        System.out.println("Largest Magical Grove Sum: " + largestGroveSum);

        // You can add more test cases by constructing different trees here
    }

    // Function to find the largest magical grove in the binary tree
    public static int findLargestMagicalGrove(TreeNode root) {
        // Reset maxSum before computation
        maxSum = 0;
        // Start the DFS traversal
        dfs(root);
        // Return the maximum sum of a valid BST subtree found
        return maxSum;
    }

    // Depth-First Search (DFS) function to analyze each subtree
    private static SubtreeInfo dfs(TreeNode node) {
        // Base case: if the node is null, return a valid BST with sum 0
        if (node == null) {
            return new SubtreeInfo(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        // Recursively get the info from left and right subtrees
        SubtreeInfo leftInfo = dfs(node.left);
        SubtreeInfo rightInfo = dfs(node.right);

        // Check if the current subtree is a valid BST
        boolean isCurrentBST = leftInfo.isBST && rightInfo.isBST &&
                leftInfo.max < node.value && node.value < rightInfo.min;

        // Calculate the current subtree sum if it is a valid BST
        int currentSum = node.value;
        if (isCurrentBST) {
            currentSum += leftInfo.sum + rightInfo.sum;
            // Update the global maximum sum if needed
            maxSum = Math.max(maxSum, currentSum);
        } else {
            currentSum = 0;
        }

        // Return the subtree info
        return new SubtreeInfo(isCurrentBST, currentSum,
                Math.min(node.value, leftInfo.min),
                Math.max(node.value, rightInfo.max));
    }
}

package io.reflectoring.coderadar.dependencymap;

import io.reflectoring.coderadar.dependencymap.domain.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class NodeTest {

  private Node testRoot;

  @BeforeAll
  public void init() {
    testRoot = new Node("testRoot", "", "");
    Node testNode1_1 = new Node("testNode1_1", "testNode1_1", "");
    Node testNode1_2 = new Node("testNode1_2", "testNode1_2", "");
    Node testNode1_3 = new Node("testNode1_3", "testNode1_3", "");
    Node testNode1_4 = new Node("testNode1_4", "testNode1_4", "");
    testRoot.addToChildren(testNode1_1);
    testRoot.addToChildren(testNode1_2);
    testRoot.addToChildren(testNode1_3);
    testRoot.addToChildren(testNode1_4);

    Node testNode2_1 = new Node("testNode2_1", "testNode1_1/testNode2_1", "");
    Node testNode2_2 = new Node("testNode2_2", "testNode1_1/testNode2_2", "");
    testNode1_1.addToChildren(testNode2_1);
    testNode1_1.addToChildren(testNode2_2);

    Node testNode2_3 = new Node("testNode2_3", "testNode1_2/testNode2_3", "");
    Node testNode2_4 = new Node("testNode2_4", "testNode1_2/testNode2_4", "");
    testNode1_2.addToChildren(testNode2_3);
    testNode1_2.addToChildren(testNode2_4);
  }

  @Test
  void testGetNodeByPath() {
    Node test = testRoot.getNodeByPath("testNode1_2/testNode2_4");
    Assertions.assertNotNull(test);
    Assertions.assertEquals("testNode2_4", test.getFilename());
  }

  @Test
  void testGetNodeByPathEmptyString() {
    Node empty = testRoot.getNodeByPath("");
    Assertions.assertNull(empty);
  }

  @Test
  void testGetNodeByPathInvalidString() {
    Node empty = testRoot.getNodeByPath("jhsbdlfjs");
    Assertions.assertNull(empty);
  }

  @Test
  void testGetNodeByPathNull() {
    Node empty = testRoot.getNodeByPath(null);
    Assertions.assertNull(empty);
  }

  @Test
  void testCreateNodeByPath() {
    Node testRoot = new Node("testRoot", "", "");
    testRoot.createNodeByPath("testNode1_5/testNode2_5/testNode3_1");

    Node testNode1_5 = testRoot.getChildByName("testNode1_5");
    Node testNode2_5 = testNode1_5.getChildByName("testNode2_5");
    Node testNode3_1 = testNode2_5.getChildByName("testNode3_1");

    Assertions.assertNotNull(testNode3_1);
    Assertions.assertEquals("testNode3_1", testNode3_1.getFilename());
    Assertions.assertEquals("testNode1_5/testNode2_5/testNode3_1", testNode3_1.getPath());
  }

  @Test
  void testCreateNodeByPathEmptyString() {
    Node empty = testRoot.createNodeByPath("");
    Assertions.assertNull(empty);
  }

  @Test
  void testCreateNodeByPathNull() {
    Node empty = testRoot.createNodeByPath(null);
    Assertions.assertNull(empty);
  }

  @Test
  void testTraversePre() {
    Assertions.assertNotNull(testRoot);
    StringBuilder traversed = new StringBuilder();
    testRoot.traversePre(node -> traversed.append(node.getFilename()).append("\n"));
    String expected =
        "testRoot\n"
            + "testNode1_4\n"
            + "testNode1_3\n"
            + "testNode1_2\n"
            + "testNode2_4\n"
            + "testNode2_3\n"
            + "testNode1_1\n"
            + "testNode2_2\n"
            + "testNode2_1\n";
    Assertions.assertNotNull(traversed.toString());
    Assertions.assertEquals(expected, traversed.toString());
  }

  @Test
  void testTraversePost() {
    Assertions.assertNotNull(testRoot);
    StringBuilder traversed = new StringBuilder();
    testRoot.traversePost(node -> traversed.append(node.getFilename()).append("\n"));
    String expected =
        "testNode1_4\n"
            + "testNode1_3\n"
            + "testNode2_4\n"
            + "testNode2_3\n"
            + "testNode1_2\n"
            + "testNode2_2\n"
            + "testNode2_1\n"
            + "testNode1_1\n"
            + "testRoot\n";
    Assertions.assertNotNull(traversed.toString());
    Assertions.assertEquals(expected, traversed.toString());
  }

  @Test
  void testGetParent() {
    Assertions.assertNotNull(testRoot);
    Node testNode2_1 = testRoot.getNodeByPath("testNode1_1/testNode2_1");
    Assertions.assertNotNull(testNode2_1);
    Assertions.assertNotNull(testNode2_1.getParent(testRoot));
    Assertions.assertEquals("testNode1_1", testNode2_1.getParent(testRoot).getFilename());
  }

  @Test
  void testGetParentRootChild() {
    Assertions.assertNotNull(testRoot);
    Assertions.assertEquals(
        "testRoot", testRoot.getNodeByPath("testNode1_1").getParent(testRoot).getFilename());
  }

  @Test
  void testGetParentRoot() {
    Assertions.assertNotNull(testRoot);
    Assertions.assertNull(testRoot.getParent(testRoot));
  }
}

/**
 * 
 */
package edu.stanford.laneweb.querymap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.DescriptorToResource;
import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.QueryToDescriptor;
import edu.stanford.irt.querymap.StreamResourceMapping;
import edu.stanford.irt.querymap.TreeNumberComparator;

/**
 * @author ceyates
 * 
 */
public class QueryMapperServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private DescriptorManager manager;
	private HttpClient httpClient;
	private TreeNumberComparator comparator = new TreeNumberComparator();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		try {
			response.setContentType("text/plain");
			String query = request.getParameter("q");
			if (query == null) {
				out.println("null query");
				return;
			}
			if (query.length() == 0) {
				out.println("no query");
				return;
			}
			String abstractCountString = request.getParameter("n");
			int abstractCount = 100;
			if (null != abstractCountString) {
				try {
					abstractCount = Integer.parseInt(abstractCountString);
				} catch (NumberFormatException e) {
					//ignore, use default
				}
			}
			QueryToDescriptor queryToDescriptor = new QueryToDescriptor();
			queryToDescriptor.setHttpClient(this.httpClient);
			queryToDescriptor.setDescriptorManager(this.manager);
			queryToDescriptor.setAbstractCount(abstractCount);
			FileInputStream weightsStream = new FileInputStream(
					"/afs/ir/group/lane/beta/stage/resources/querymap/descriptor-weights.xml");
			DescriptorWeightMap descriptorWeights = new DescriptorWeightMap(
					weightsStream);
			queryToDescriptor.setDescriptorWeights(descriptorWeights);
			DescriptorToResource descriptorToResource = new DescriptorToResource();
			FileInputStream resourceMapsStream = new FileInputStream(
					"/afs/ir/group/lane/beta/stage/resources/querymap/resource-maps.xml");
			StreamResourceMapping resourceMapping = new StreamResourceMapping(
					resourceMapsStream);
			descriptorToResource.setResourceMap(resourceMapping);

			Set<Descriptor> descriptors = queryToDescriptor
					.getDescriptorsForQuery(query);
			Descriptor descriptor = null;
			if (descriptors.size() > 0) {
				descriptor = descriptors.iterator().next();
			} else {
				out.println(query + " did not match any MeSH terms.");
				return;
			}
			Set<String> resources = descriptorToResource
					.getResourceForDescriptor(descriptor);
			Descriptor matchedDescriptor = null;
			if (resources != null && resources.size() > 0) {
				matchedDescriptor = descriptor;
			}
			out.println(query + " matched " + descriptor);
			out.println();
			out.println("parent MeSH terms:");
			Set<String> trees = new TreeSet<String>(this.comparator);
			for (String treeNumber : descriptor.getTreeNumbers()) {
				if (treeNumber.indexOf('.') > -1) {
					while (treeNumber.indexOf('.') > -1) {
						treeNumber = treeNumber.substring(0, treeNumber
								.lastIndexOf('.'));
						trees.add(treeNumber);
					}
				}
			}
			for (String treeNumber : trees) {
				descriptor = this.manager.getDescriptorForTree(treeNumber);
				out.println("     " + treeNumber + ": " + descriptor);
				if (resources == null || resources.size() ==0) {
					resources = descriptorToResource
							.getResourceForDescriptor(descriptor);
					matchedDescriptor = descriptor;
				}
			}
			out.println();
			if (resources != null && resources.size() > 0) {
				out.println("the query: " + query);
				out.println("found a resource match: " + resources);
				out.println("associated with: " + matchedDescriptor);
			} else {
				out.println("no resources found to match the query: " + query);
			}
			out.println();
			out.println("descriptors sorted by weight:");
			for (Descriptor desc : descriptors) {
				out.println(desc);
			}
		} catch (Exception e) {
			e.printStackTrace(out);
		}
	}

	@Override
	public void init() throws ServletException {
		this.httpClient = new HttpClient(
				new MultiThreadedHttpConnectionManager());
		this.manager = new DescriptorManager();
	}
}

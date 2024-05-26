package us.muit.fs.a4i.test.model.remote;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositoryStatistics;
import org.kohsuke.github.GitHub;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.model.entities.ReportI;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestGitHubRepositoryEnquirer {

    @Mock
    private GitHub gitHubMock;

    @Mock
    private GHRepository ghRepositoryMock;

    @Mock
    private GHRepositoryStatistics ghRepositoryStatisticsMock;

    @InjectMocks
    private GitHubRepositoryEnquirer gitHubRepositoryEnquirer;

    @BeforeEach
    void setup() {
        when(gitHubMock.getRepository(anyString())).thenReturn(ghRepositoryMock);
    }

    @Test
    void testBuildReport() throws IOException {
        // Mock repository data
        when(ghRepositoryMock.getSubscribersCount()).thenReturn(100);
        when(ghRepositoryMock.getForksCount()).thenReturn(200);
        when(ghRepositoryMock.getWatchersCount()).thenReturn(300);
        when(ghRepositoryMock.getStargazersCount()).thenReturn(400);
        when(ghRepositoryMock.getOpenIssueCount()).thenReturn(500);
        when(ghRepositoryMock.getIssues()).thenReturn(new PagedIterable<GHIssue>() {
            @Override
            public PagedIterator<GHIssue> _iterator(int pageSize) {
                return null;
            }
        });
        when(ghRepositoryMock.getCreatedAt()).thenReturn(new Date());
        when(ghRepositoryMock.getPushedAt()).thenReturn(new Date());
        when(ghRepositoryMock.getUpdatedAt()).thenReturn(new Date());
        when(ghRepositoryMock.getStatistics()).thenReturn(ghRepositoryStatisticsMock);

        // Execute
        ReportI report = gitHubRepositoryEnquirer.buildReport("test/repo");

        // Verify metrics
        assertNotNull(report);
        assertEquals(100, report.getMetric("subscribers").getValue());
        assertEquals(200, report.getMetric("forks").getValue());
        assertEquals(300, report.getMetric("watchers").getValue());
        assertEquals(400, report.getMetric("stars").getValue());
        assertEquals(500, report.getMetric("issues").getValue());
    }

    @Test
    void testGetMetric_Subscribers() throws MetricException, IOException {
        // Mock repository data
        when(ghRepositoryMock.getSubscribersCount()).thenReturn(100);

        // Execute
        Metric metric = gitHubRepositoryEnquirer.getMetric("subscribers", "test/repo");

        // Verify
        assertNotNull(metric);
        assertEquals("subscribers", metric.getName());
        assertEquals(100, metric.getValue());
    }

    @Test
    void testGetMetric_InvalidMetric() {
        assertThrows(MetricException.class, () -> {
            gitHubRepositoryEnquirer.getMetric("invalidMetric", "test/repo");
        });
    }
}
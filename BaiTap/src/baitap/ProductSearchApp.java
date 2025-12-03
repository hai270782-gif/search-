/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baitap;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.*;
import java.util.List;

public class ProductSearchApp extends JFrame {
    private JTextField productField;
    private JButton searchBtn, compareBtn, savedListBtn;
    private JList<String> resultList;
    private DefaultListModel<String> resultListModel;
    private JPopupMenu resultPopup;
    private JLabel pageInfoLabel;
    private JButton prevPageBtn, nextPageBtn;

    private int currentPage = 1;
    private int totalPages = 1;
    private static final int PAGE_SIZE = 8;

    private List<SearchResult> allResults = new ArrayList<>();
    private List<SearchResult> currentPageResults = new ArrayList<>();
    private List<SearchResult> displayedResults = new ArrayList<>();

    public ProductSearchApp() {
        setTitle("Tìm kiếm sản phẩm đa sàn");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // CENTER - MIDDLE
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel keywordLabel = new JLabel("Nhập từ khóa / tên sản phẩm:");
        keywordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        keywordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        productField = new JTextField(20);
        searchBtn = new JButton("Tìm kiếm");
        searchRow.add(productField);
        searchRow.add(searchBtn);

        middlePanel.add(keywordLabel);
        middlePanel.add(Box.createVerticalStrut(10));
        middlePanel.add(searchRow);
        middlePanel.add(Box.createVerticalStrut(20));

        compareBtn = new JButton("So sánh sản phẩm");
        compareBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(compareBtn);
        middlePanel.add(Box.createVerticalStrut(10));

        savedListBtn = new JButton("Các web đã lưu");
        savedListBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(savedListBtn);

        mainPanel.add(middlePanel, BorderLayout.NORTH);

        // RIGHT PANEL - Kết quả
        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);
        resultList.setFixedCellHeight(30);
        JScrollPane scroll = new JScrollPane(resultList);

        prevPageBtn = new JButton("Trang trước");
        nextPageBtn = new JButton("Trang sau");
        pageInfoLabel = new JLabel("Trang 0 / 0 (0 kết quả)");

        JPanel pagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pagePanel.add(prevPageBtn);
        pagePanel.add(pageInfoLabel);
        pagePanel.add(nextPageBtn);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(scroll, BorderLayout.CENTER);
        rightPanel.add(pagePanel, BorderLayout.SOUTH);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Popup lưu web
        resultPopup = new JPopupMenu();
        JMenuItem saveItem = new JMenuItem("Lưu web này");
        saveItem.addActionListener(e -> saveSelectedWeb());
        resultPopup.add(saveItem);

        resultList.addMouseListener(new MouseAdapter() {
            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int index = resultList.locationToIndex(e.getPoint());
                    if (index >= 0 && index < displayedResults.size()) {
                        resultList.setSelectedIndex(index);
                        resultPopup.show(resultList, e.getX(), e.getY());
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { showPopup(e); }
            @Override
            public void mouseReleased(MouseEvent e) { showPopup(e); }
        });

        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    int index = resultList.locationToIndex(e.getPoint());
                    if (index >= 0 && index < displayedResults.size()) {
                        openInBrowser(displayedResults.get(index).url);
                    }
                }
            }
        });

        searchBtn.addActionListener(e -> performSearch());
        prevPageBtn.addActionListener(e -> goToPage(currentPage - 1));
        nextPageBtn.addActionListener(e -> goToPage(currentPage + 1));
        compareBtn.addActionListener(e -> openCompareDialog());

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void performSearch() {
        String keyword = productField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ khóa",
                    "Thiếu từ khóa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Demo: tạo dữ liệu giả lập cho Shopee, TikTok, Lazada
        allResults.clear();
        for (int i = 1; i <= 23; i++) {
            allResults.add(new SearchResult("Shopee", keyword + " Sản phẩm " + i, 100000L + i*5000, "https://shopee.vn/p/" + i));
            allResults.add(new SearchResult("TikTok", keyword + " Sản phẩm " + i, 95000L + i*4000, "https://tiktok.com/item/" + i));
            allResults.add(new SearchResult("Lazada", keyword + " Sản phẩm " + i, 105000L + i*6000, "https://lazada.vn/product/" + i));
        }
        currentPage = 1;
        refreshResultPage();
    }

    private void refreshResultPage() {
        resultListModel.clear();
        displayedResults.clear();

        if (allResults.isEmpty()) {
            pageInfoLabel.setText("Trang: 0 / 0 (0 kết quả)");
            prevPageBtn.setEnabled(false);
            nextPageBtn.setEnabled(false);
            return;
        }

        int total = allResults.size();
        totalPages = (total + PAGE_SIZE - 1) / PAGE_SIZE;
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int start = (currentPage - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, total);
        for (int i = start; i < end; i++) {
            SearchResult r = allResults.get(i);
            displayedResults.add(r);
            resultListModel.addElement(r.site + " - " + r.title + " - " + r.price + " đ");
        }

        pageInfoLabel.setText("Trang: " + currentPage + " / " + totalPages + " (" + total + " kết quả)");
        prevPageBtn.setEnabled(currentPage > 1);
        nextPageBtn.setEnabled(currentPage < totalPages);
    }

    private void goToPage(int page) {
        currentPage = page;
        refreshResultPage();
    }

    private void saveSelectedWeb() {
        int index = resultList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một sản phẩm trước khi lưu");
            return;
        }
        SearchResult r = displayedResults.get(index);
        SavedWebManager.add(r);
        JOptionPane.showMessageDialog(this, "Đã lưu: " + r.site);
    }

    private void openCompareDialog() {
        CompareDialog dialog = new CompareDialog(this, allResults);
        dialog.setVisible(true);
    }

    private void openInBrowser(String url) {
        try { Desktop.getDesktop().browse(new URI(url)); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Không mở được trình duyệt"); }
    }

    public static void main(String[] args) {
        new ProductSearchApp();
    }
}

// ========================== Helper Classes ==========================
class SearchResult {
    String site, title, url;
    long price;
    public SearchResult(String site, String title, long price, String url) {
        this.site = site; this.title = title; this.price = price; this.url = url;
    }
}

class SavedWebManager {
    private static final List<SearchResult> saved = new ArrayList<>();
    public static void add(SearchResult r) { saved.add(r); }
    public static List<SearchResult> getAll() { return new ArrayList<>(saved); }
}

// ========================== Compare Dialog ==========================
class CompareDialog extends JDialog {
    private JComboBox<String> site1Combo, site2Combo;
    private JLabel compareLabel;
    private List<SearchResult> allResults;

    public CompareDialog(JFrame owner, List<SearchResult> results) {
        super(owner, "So sánh sản phẩm", true);
        allResults = results;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel();
        top.add(new JLabel("Chọn 2 web để so sánh:"));
        site1Combo = new JComboBox<>(getSites());
        site2Combo = new JComboBox<>(getSites());
        top.add(site1Combo);
        top.add(site2Combo);

        JButton compareBtn = new JButton("So sánh");
        top.add(compareBtn);
        add(top, BorderLayout.NORTH);

        compareLabel = new JLabel(" ");
        compareLabel.setVerticalAlignment(SwingConstants.TOP);
        add(compareLabel, BorderLayout.CENTER);

        compareBtn.addActionListener(e -> compare());
    }

    private String[] getSites() {
        Set<String> sites = new LinkedHashSet<>();
        for (SearchResult r : allResults) sites.add(r.site);
        return sites.toArray(new String[0]);
    }

    private void compare() {
        String s1 = (String) site1Combo.getSelectedItem();
        String s2 = (String) site2Combo.getSelectedItem();
        if (s1.equals(s2)) { JOptionPane.showMessageDialog(this, "Chọn 2 web khác nhau"); return; }

        SearchResult r1 = allResults.stream().filter(r -> r.site.equals(s1))
                .min(Comparator.comparingLong(r -> r.price)).orElse(null);
        SearchResult r2 = allResults.stream().filter(r -> r.site.equals(s2))
                .min(Comparator.comparingLong(r -> r.price)).orElse(null);
        if (r1 == null || r2 == null) return;

        String diff = String.format("%,d", Math.abs(r1.price - r2.price)).replace(',', '.') + " đ";
        String cheaper = r1.price < r2.price ? r1.site : r2.site;
        compareLabel.setText("<html>" + s1 + ": " + r1.price + " đ<br>" + s2 + ": " + r2.price + " đ<br>Web rẻ hơn: " + cheaper + " (" + diff + ")</html>");
    }
}

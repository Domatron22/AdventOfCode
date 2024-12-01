use std::fs::File;
use std::io::{BufRead, BufReader};
use std::path::Path;

fn parse_lists_from_file(filename: &str) -> Result<(Vec<i32>, Vec<i32>), Box<dyn std::error::Error>> {
    let path = Path::new(filename);
    let file = File::open(path)?;
    let reader = BufReader::new(file);

    let mut left_list = Vec::new();
    let mut right_list = Vec::new();

    for line in reader.lines() {
        let line = line?;
        let parts: Vec<&str> = line.trim().split_whitespace().collect();

        if parts.len() == 2 {
            if let (Ok(left), Ok(right)) = (parts[0].parse::<i32>(), parts[1].parse::<i32>()) {
                left_list.push(left);
                right_list.push(right);
            }
        }
    }

    Ok((left_list, right_list))
}

fn calculate_distance(left_list: &[i32], right_list: &[i32]) -> i32 {
    // Sort both lists
    let mut sorted_left = left_list.to_vec();
    let mut sorted_right = right_list.to_vec();
    sorted_left.sort_unstable();
    sorted_right.sort_unstable();

    // Calculate total distance
    sorted_left.iter()
        .zip(sorted_right.iter())
        .map(|(left, right)| (left - right).abs())
        .sum()
}

fn calculate_similarity_score(left_list: &[i32], right_list: &[i32]) -> i32 {
    left_list.iter()
        .map(|left | (left * right_list.iter().filter(|x| **x == *left).count() as i32).abs())
        .sum()

}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let (left_list, right_list) = parse_lists_from_file("src/main/2024/resources/01/input.txt")?;

    // println!("Left List: {:?}", left_list);
    // println!("Right List: {:?}", right_list);

    let total_distance = calculate_distance(&left_list, &right_list);
    println!("Total distance: {}", total_distance);

    let similarity_score = calculate_similarity_score(&left_list, &right_list);
    println!("Similarity Score: {}", similarity_score);

    Ok(())
}